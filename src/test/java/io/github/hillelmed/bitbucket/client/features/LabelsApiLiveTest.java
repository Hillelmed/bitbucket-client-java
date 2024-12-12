package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "LabelsApiLiveTest", singleThreaded = true)
public class LabelsApiLiveTest extends BaseBitbucketApiLiveTest {

    @Test
    public void testLabel() {
        Assertions.assertThat(api().list(null).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void testLabelByName() {
        try {
            Assertions.assertThat(Objects.requireNonNull(api().getLabelByName("labelName").getBody()).getName()).isEmpty();
        } catch (BitbucketAppException e) {
            assertThat(e.code().is4xxClientError()).isTrue();
        }
    }

    private LabelsApi api() {
        return bitbucketClient.api().labelsApi();
    }
}
