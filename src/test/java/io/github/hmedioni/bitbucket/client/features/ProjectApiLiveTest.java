package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.project.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "ProjectApiLiveTest", singleThreaded = true)
public class ProjectApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String testGetProjectKeyword = "testGetProject";
    private final String projectWriteKeyword = "PROJECT_WRITE";
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private User user;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.user = TestUtilities.getDefaultUser(bitbucketClient.getBitbucketAuthentication(), bitbucketClient.api());
    }

    @Test
    public void testGetProject() {
        final Project project = api().get(generatedTestContents.project.getKey()).getBody();
        assertThat(project).isNotNull();
        assertThat(project.getKey()).isEqualTo(generatedTestContents.project.getKey());
    }

    @Test
    public void testListProjects() {
        final ProjectPage projectPage = api().list(null, null, 0, 100).getBody();

        assertThat(projectPage).isNotNull();
        Assertions.assertThat(projectPage.getSize()).isPositive();

        final List<Project> projects = projectPage.getValues();
        assertThat(projects).isNotEmpty();
        boolean found = false;
        for (final Project possibleProject : projectPage.getValues()) {
            if (possibleProject.getKey().equals(generatedTestContents.project.getKey())) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }

    @Test
    public void testDeleteProjectNonExistent() {
        try {
            final ResponseEntity<Void> success = api().delete(TestUtilities.randomStringLettersOnly());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test
    public void testGetProjectNonExistent() {
        try {
            final Project project = api().get(TestUtilities.randomStringLettersOnly()).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }

    }

    @Test
    public void testCreateProjectWithIllegalName() {
        if (!generatedTestContents.projectPreviouslyExists) {
            final String illegalProjectKey = "9999";
            final CreateProject createProject = new CreateProject(illegalProjectKey, null, null, null);
            try {
                final Project project = api().create(createProject).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e.errors().isEmpty()).isFalse();
            }
        } else {
            System.out.println("Project previously existed and so assuming we don't have credentials to create Projects");
        }
    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testCreatePermissionByUser() {
        final ResponseEntity<Void> success = api().createPermissionsByUser(projectKey, projectWriteKeyword, user.getSlug());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = "testCreatePermissionByUser")
    public void testListPermissionByUser() {
        final ProjectPermissionsPage projectPermissionsPage = api().listPermissionsByUser(projectKey, 0, 100);
        Assertions.assertThat(projectPermissionsPage.getValues()).isNotEmpty();
    }

    @Test(dependsOnMethods = "testListPermissionByUser")
    public void testDeletePermissionByUser() {
        final ResponseEntity<Void> success = api().deletePermissionsByUser(projectKey, user.getSlug());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testCreatePermissionByGroup() {
        final ResponseEntity<Void> success = api().createPermissionsByGroup(projectKey, projectWriteKeyword, defaultBitbucketGroup);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = "testCreatePermissionByGroup")
    public void testListPermissionByGroup() {
        final ProjectPermissionsPage projectPermissionsPage = api().listPermissionsByGroup(projectKey, 0, 100).getBody();
        Assertions.assertThat(projectPermissionsPage.getValues()).isNotEmpty();
    }

    @Test(dependsOnMethods = "testListPermissionByGroup")
    public void testDeletePermissionByGroup() {
        final ResponseEntity<Void> success = api().deletePermissionsByGroup(projectKey, defaultBitbucketGroup);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testCreatePermissionByGroupNonExistent() {
        try {
            final ResponseEntity<Void> success = api().createPermissionsByGroup(projectKey, projectWriteKeyword, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testDeletePermissionByGroupNonExistent() {
        final ResponseEntity<Void> success = api().deletePermissionsByGroup(projectKey, TestUtilities.randomString());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testCreatePermissionByUserNonExistent() {
        try {
            final ResponseEntity<Void> success = api().createPermissionsByUser(projectKey, projectWriteKeyword, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = testGetProjectKeyword)
    public void testDeletePermissionByUserNonExistent() {
        try {
            final ResponseEntity<Void> success = api().deletePermissionsByUser(projectKey, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private ProjectApi api() {
        return api.projectApi();
    }
}
