package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "KeysApiLiveTest", singleThreaded = true)
public class KeysApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String testPubKey =
            "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCy9f0/nwkXESzkL4v4ftZ24VJYvkQ/Nt6vsLab3iSWtJXqrRsBythCcbAU6W9"
                    + "5OGxjbTSFFtp0poqMcPuogocMR7QhjY9JGG3fcnJ7nYDCGRHD4zfG5Af/tHwvJ2ew0WTYoemvlfZIG/jZ7fsuOQSyUpJoxGAlb6"
                    + "/QpnfSmJjxCx0VEoppWDn8CO3VhOgzVhWx0ecne+ZcUy3Ktt3HBQN0hosRfqkVSRTvkpK4RD8TaW5PrVDe1r2Q5ab37TO+Ls4xx"
                    + "t16QlPubNxWeH3dHVzXdmFAItuH0DuyLyMoW1oxZ6+NrKu+pAAERxM303gejFzKDqXid5m1EOTvk4xhyqYN user@host";
    private final String label = "me@127.0.0.1";
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private Long repoKeyId;
    private Long projectKeyId;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
    }

    @Test
    public void testAddSSHKeyForUser() {
        final Key key = Key.create(testPubKey, label, testPubKey.length(), 20, "RSA");
        final ResponseEntity<Key> responseKey = api().createSSHKeyForUser(bitbucketProperties.getUser(), key);
        assertThat(responseKey.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseKey.getBody().getId()).isNotNull();
    }

    @Test(dependsOnMethods = "testAddSSHKeyForUser")
    public void testDeleteAllSSHKeysForUser() {
        final ResponseEntity<Void> responseKey = api().deleteAllSSHKeysForUser(bitbucketProperties.getUser());
        assertThat(responseKey.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test(dependsOnMethods = "testDeleteAllSSHKeysForUser")
    public void testAddForRepository() {
        final AccessKey newKey = api().createForRepo(projectKey, repoKey,
                new CreateAccessKey(new CreateKey(testPubKey), AccessKey.PermissionType.REPO_READ)).getBody();

        assertThat(newKey).isNotNull();
        assertThat(newKey.getKey().getId()).isNotNull();

        this.repoKeyId = newKey.getKey().getId();
    }


    @Test
    public void testErrorExpireDaysMustBePositiveAddKeyForUser() {
        final Key key = Key.create(testPubKey, label, testPubKey.length(), -1, "RSA");
        try {
            api().createSSHKeyForUser(bitbucketProperties.getUser(), key);
        } catch (BitbucketAppException e) {
            assertThat(e.errors().size()).isPositive();
        }
    }


    @Test(dependsOnMethods = "testAddForRepository")
    public void testGetForRepository() {
        final AccessKey key = api().getForRepo(projectKey, repoKey, repoKeyId).getBody();

        assertThat(key).isNotNull();
        assertThat(key.getKey().getId()).isNotNull();
        assertThat(key.getKey().getId().equals(repoKeyId)).isTrue();
    }

    @Test(dependsOnMethods = "testGetForRepository")
    public void testListByRepository() {
        final AccessKeyPage accessKeyPage = api().listByRepo(projectKey, repoKey, 0, 25).getBody();

        assertThat(accessKeyPage).isNotNull();
        Assertions.assertThat(accessKeyPage.getSize()).isPositive();

        Assertions.assertThat(accessKeyPage.getValues()).isNotEmpty();
        boolean found = false;
        for (final AccessKey possibleAccessKey : accessKeyPage.getValues()) {
            if (possibleAccessKey.getKey().getId().equals(repoKeyId)) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }

    @Test(dependsOnMethods = "testListByRepository")
    public void testDeleteFromRepository() {
        final ResponseEntity<Void> success = api().deleteFromRepo(projectKey, repoKey, repoKeyId);

        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
        try {
            final AccessKey missingKey = api().getForRepo(projectKey, repoKey, repoKeyId).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = "testDeleteAllSSHKeysForUser")
    public void testAddForProject() {
        final AccessKey newKey = api().createForProject(projectKey,
                new CreateAccessKey(new CreateKey(testPubKey), AccessKey.PermissionType.PROJECT_READ)).getBody();

        assertThat(newKey).isNotNull();
        assertThat(newKey.getKey().getId()).isNotNull();

        this.projectKeyId = newKey.getKey().getId();
    }

    @Test(dependsOnMethods = "testAddForProject")
    public void testGetForProject() {
        final AccessKey key = api().getForProject(projectKey, projectKeyId).getBody();

        assertThat(key).isNotNull();
        assertThat(key.getKey().getId()).isNotNull();
        assertThat(key.getKey().getId().equals(projectKeyId)).isTrue();
    }

    @Test(dependsOnMethods = "testGetForProject")
    public void testListByProject() {
        final AccessKeyPage accessKeyPage = api().listByProject(projectKey, 0, 25).getBody();

        assertThat(accessKeyPage).isNotNull();
        Assertions.assertThat(accessKeyPage.getSize()).isGreaterThan(0);

        Assertions.assertThat(accessKeyPage.getValues()).isNotEmpty();
        boolean found = false;
        for (final AccessKey possibleAccessKey : accessKeyPage.getValues()) {
            if (possibleAccessKey.getKey().getId().equals(projectKeyId)) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }

    @Test(dependsOnMethods = "testListByProject")
    public void testDeleteFromProject() {
        final ResponseEntity<Void> success = api().deleteFromProject(projectKey, projectKeyId);

        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
        try {
            final AccessKey missingKey = api().getForProject(projectKey, projectKeyId).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private KeysApi api() {
        return bitbucketClient.api().keysApi();
    }
}
