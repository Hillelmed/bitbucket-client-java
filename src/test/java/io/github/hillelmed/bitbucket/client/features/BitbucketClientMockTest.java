package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.config.*;
import org.testng.annotations.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Test(groups = "unit", testName = "BitbucketClientMockTest")
public class BitbucketClientMockTest extends BaseBitbucketMockTest {

    @Test
    public void testCreateClient() {
        final BitbucketClient client = BitbucketClient.create(new BitbucketProperties());
        assertThat(client).isNotNull();
    }
}
