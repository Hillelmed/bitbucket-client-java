package io.github.hillelmed.bitbucket.client.features;


import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.admin.*;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;


@Test(groups = "live", testName = "AdminApiLiveTest", singleThreaded = true)
public class AdminApiLiveTest extends BaseBitbucketApiLiveTest {

    private static final String TEST_USER_NAME = "TestUserName";

    @Test
    public void testListUsersByGroup() {
        final UserPage userPage = api().listUsersByGroup(defaultBitbucketGroup, null, null, null).getBody();
        assertThat(userPage).isNotNull();
        assertThat(userPage.getSize() > 0).isTrue();
    }

    @Test
    public void testListUsersByNonExistentGroup() {
        final UserPage userPage = api().listUsersByGroup(TestUtilities.randomString(), null, null, null).getBody();
        assertThat(userPage).isNotNull();
        assertThat(userPage.getSize() == 0).isTrue();
    }

    @Test
    public void testListUsers() {
        final User user = TestUtilities.getDefaultUser(this.bitbucketClient.getBitbucketAuthentication(), this.bitbucketClient.api());
        if (user != null) {
            final UserPage userPage = api().listUsers(user.getSlug(), null, null).getBody();
            assertThat(userPage).isNotNull();
            assertThat(userPage.getSize() > 0).isTrue();
        } else {
            fail("Failed to get user");
        }
    }

    @Test
    public void testListUsersNonExistent() {
        final UserPage userPage = api().listUsers(TestUtilities.randomString(), null, null).getBody();
        assertThat(userPage).isNotNull();
        assertThat(userPage.getSize() == 0).isTrue();
    }

    @Test
    public void testCreateUser() {
        final ResponseEntity<Void> requestStatus = api().createUser(TEST_USER_NAME, TEST_USER_NAME, TEST_USER_NAME,
                "testUser@test.test", null, null);
        assertThat(requestStatus).isNotNull();
        assertThat(requestStatus.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test(dependsOnMethods = "testCreateUser")
    public void testListSpecificUser() {
        final UserPage userPage = api().listUsers(TEST_USER_NAME, null, null).getBody();
        assertThat(userPage).isNotNull();
        assertThat(userPage.getSize() > 0).isTrue();
    }

    @Test(dependsOnMethods = "testListSpecificUser")
    public void testDeleteUser() {
        final User deletedUser = api().deleteUser(TEST_USER_NAME).getBody();
        assertThat(deletedUser).isNotNull();
    }

    private AdminApi api() {
        return bitbucketClient.api().adminApi();
    }
}
