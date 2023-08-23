package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "WebHookApiLiveTest", singleThreaded = true)
public class WebHookApiLiveTest extends BaseBitbucketApiLiveTest {

    private final List<WebHook.EventType> eventTypes = List.of(WebHook.EventType.PR_COMMENT_ADDED);
    private final CreateWebHook createWebHook = CreateWebHook.create(TestUtilities.randomStringLettersOnly(),
            eventTypes,
            "http://www.google.com",
            true,
            null);
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;

    private WebHook webHook;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(this.bitbucketClient, bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
    }

    @Test
    public void testCreateWebHook() {
        this.webHook = api().create(projectKey, repoKey, createWebHook).getBody();
        assertThat(webHook).isNotNull();
    }

    @Test
    public void testCreateWebHookOnError() {
        try {
            final WebHook ref = api().create(projectKey, TestUtilities.randomStringLettersOnly(), createWebHook).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testCreateWebHook")
    public void testGetWebHook() {
        final WebHook ref = api().get(projectKey, repoKey, webHook.getId()).getBody();
        assertThat(ref).isNotNull();
    }

    @Test
    public void testGetWebHookOnError() {
        try {
            final WebHook ref = api().get(projectKey, TestUtilities.randomStringLettersOnly(), TestUtilities.randomStringLettersOnly()).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testGetWebHook")
    public void testListWebHooks() throws Exception {

        final List<WebHook> allWebHooks = new ArrayList<>();
        Integer start = null;
        while (true) {
            final WebHookPage ref = api().list(projectKey, repoKey, start, 100).getBody();

            allWebHooks.addAll(ref.getValues());
            start = ref.getNextPageStart();
            if (ref.isLastPage()) {
                break;
            } else {
                System.out.println("Sleeping for 1 seconds before querying for next page");
                Thread.sleep(1000);
            }
        }
        assertThat(allWebHooks.size() > 0).isEqualTo(true);
    }

    @Test
    public void testListWebHooksOnError() {
        try {
            final WebHookPage ref = api().list(projectKey, TestUtilities.randomStringLettersOnly(), null, 100).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testListWebHooks")
    public void testUpdateWebHook() {
        final List<WebHook.EventType> updateEventTypes = List.of(WebHook.EventType.PR_COMMENT_DELETED);
        final CreateWebHook updateWebHook = CreateWebHook.create(webHook.getName(),
                updateEventTypes,
                "http://www.google.com",
                true,
                null);

        this.webHook = api().update(projectKey, repoKey, webHook.getId(), updateWebHook).getBody();
        assertThat(webHook).isNotNull();
    }

    @Test
    public void testUpdateWebHookOnError() {
        try {
            final WebHook ref = api().update(projectKey,
                    TestUtilities.randomStringLettersOnly(),
                    TestUtilities.randomStringLettersOnly(),
                    createWebHook).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testUpdateWebHook")
    public void testDeleteWebHook() {
        final ResponseEntity<Void> ref = api().delete(projectKey, repoKey, webHook.getId());
        assertThat(ref).isNotNull();
        assertThat(ref.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testDeleteWebHookOnError() {
        try {
            final ResponseEntity<Void> ref = api().delete(projectKey,
                    TestUtilities.randomStringLettersOnly(),
                    TestUtilities.randomStringLettersOnly());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private WebHookApi api() {
        return api.webHookApi();
    }
}
