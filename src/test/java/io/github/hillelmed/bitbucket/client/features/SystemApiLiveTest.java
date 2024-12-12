package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.system.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "SystemApiLiveTest", singleThreaded = true)
public class SystemApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String versionRegex = "^\\d+\\.\\d+\\.\\d+$";

    @Test
    public void testGetVersion() {
        final Version version = api().version().getBody();
        assertThat(version).isNotNull();
        assertThat(version.getId()).matches(versionRegex);
    }

    private SystemApi api() {
        return api.systemApi();
    }
}
