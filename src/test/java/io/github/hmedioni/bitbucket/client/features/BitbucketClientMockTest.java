package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.config.*;
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
