package io.github.hmedioni.bitbucket.client;

import com.fasterxml.jackson.databind.*;
import io.github.hmedioni.bitbucket.client.config.*;
import org.springframework.test.context.testng.*;
import org.testng.annotations.*;

@Test(groups = "live")
public class BaseBitbucketApiLiveTest extends AbstractTestNGSpringContextTests {

    protected final String defaultBitbucketGroup = "stash-users";
    final protected ObjectMapper objectMapper = new ObjectMapper();
    final protected String url = "http://127.0.0.1:7990";
    final private String user = "hillelMed";
    final private String password = "hh15975352";
    protected BitbucketProperties bitbucketProperties = new BitbucketProperties(url, user, password);
    protected BitbucketClient bitbucketClient = BitbucketClient.create(bitbucketProperties);
    protected BitbucketApi api = bitbucketClient.api();

}
