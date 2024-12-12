package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.auth.*;
import org.testng.annotations.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Test(groups = "unit", testName = "BitbucketAuthenticationMockTest")
public class BitbucketAuthenticationMockTest extends BaseBitbucketMockTest {

    private final String unencodedAuth = "hello:world";
    private final String encodedAuth = "aGVsbG86d29ybGQ=";

    public void testCreateAnonymousAuth() {
        final BitbucketAuthentication auth = BitbucketAuthentication.builder().build();
        assertThat(auth).isNotNull();
        assertThat(auth.authValue()).isEmpty();
        assertThat(auth.authType()).isEqualTo(AuthenticationType.ANONYMOUS);
    }

    public void testCreateBasicAuthUnencoded() {
        final BitbucketAuthentication auth = BitbucketAuthentication.builder().credentials(unencodedAuth).build();
        assertThat(auth).isNotNull();
        assertThat(auth.authValue()).isNotNull();
        assertThat(auth.authValue()).isNotEqualTo(unencodedAuth);
        assertThat(auth.authValue()).isEqualTo(encodedAuth);
        assertThat(auth.authType()).isEqualTo(AuthenticationType.BASIC);
    }

    public void testCreateBasicAuthEncoded() {
        final BitbucketAuthentication auth = BitbucketAuthentication.builder().credentials(unencodedAuth).build();
        assertThat(auth).isNotNull();
        assertThat(auth.authValue()).isNotNull();
        assertThat(auth.authValue()).isEqualTo(encodedAuth);
        assertThat(auth.authType()).isEqualTo(AuthenticationType.BASIC);
    }
}
