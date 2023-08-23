package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.tags.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "TagApiLiveTest", singleThreaded = true)
public class TagApiLiveTest extends BaseBitbucketApiLiveTest {

    final String tagName = TestUtilities.randomStringLettersOnly();
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private String commitHash;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(bitbucketClient, bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();

        final CommitPage commitPage = api.commitsApi().list(projectKey, repoKey, true, null, null, null, null, null, null, 1, 0).getBody();
        Assertions.assertThat(commitPage.getValues().isEmpty()).isFalse();
        this.commitHash = commitPage.getValues().get(0).getId();
    }

    @Test
    public void testCreateTag() {
        final CreateTag createTag = new CreateTag(tagName, commitHash, null);
        final Tag tag = api().create(projectKey, repoKey, createTag).getBody();
        assertThat(tag).isNotNull();
        assertThat(tag.getId().endsWith(tagName)).isTrue();
        assertThat(commitHash.equalsIgnoreCase(tag.getLatestCommit())).isTrue();
    }

    @Test(dependsOnMethods = "testCreateTag")
    public void testGetTag() {
        final Tag tag = api().get(projectKey, repoKey, tagName).getBody();
        assertThat(tag).isNotNull();
        assertThat(tag.getId().endsWith(tagName)).isTrue();
        assertThat(commitHash.equalsIgnoreCase(tag.getLatestCommit())).isTrue();
    }

    @Test
    public void testGetTagNonExistent() {
        try {
            final Tag tag = api().get(projectKey, repoKey, tagName + "9999").getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testGetTag")
    public void testListTags() {
        final TagPage tagPage = api().list(projectKey, repoKey, null, null, 0, 10).getBody();
        assertThat(tagPage).isNotNull();
        Assertions.assertThat(tagPage.getValues()).isNotEmpty();
    }

    @Test(dependsOnMethods = "testGetTag")
    public void testDeleteTag() {
        final ResponseEntity<Void> status = api().delete(projectKey, repoKey, tagName);
        assertThat(status).isNotNull();
        assertThat(status.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testDeleteTagNonExistent() {
        try {
            final ResponseEntity<Void> status = api().delete(projectKey, repoKey, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test
    public void testListTagsNonExistentRepo() {
        try {
            final TagPage tagPage = api().list(projectKey, TestUtilities.randomString(), null, null, 0, 10).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private TagApi api() {
        return api.tagApi();
    }
}
