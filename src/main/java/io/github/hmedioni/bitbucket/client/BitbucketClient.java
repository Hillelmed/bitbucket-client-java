package io.github.hmedioni.bitbucket.client;

import io.github.hmedioni.bitbucket.client.auth.*;
import io.github.hmedioni.bitbucket.client.config.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.filters.*;
import lombok.*;
import org.springframework.lang.*;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.*;
import org.springframework.web.service.invoker.*;
import org.springframework.web.util.*;

import java.io.*;

import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BitbucketClient implements Closeable {


    private BitbucketApi bitbucketApi;
    @Getter
    private BitbucketAuthentication bitbucketAuthentication;

    /**
     * Create an BitbucketClient. If any of the passed in variables are null we
     * will query System Properties and Environment Variables, in order, to
     * search for values that may be set in a devops/CI fashion. The only
     * difference is the `overrides` which gets merged, but takes precedence,
     * with those System Properties and Environment Variables found.
     * <p>
     * //     * @param endPoint       URL of Bitbucket instance.
     * //     * @param authentication authentication used to connect to Bitbucket instance.
     * //     * @param overrides      jclouds Properties to override defaults when creating a new BitbucketApi.
     * //     * @param modules        a list of modules to be passed to the Contextbuilder, e.g. for logging.
     */
    private BitbucketClient(@Nullable BitbucketProperties bitbucketProperties, BitbucketApi bitbucketApi) {
        BitbucketAuthentication bitbucketAuthentication;
        if (bitbucketProperties != null) {
            if (bitbucketProperties.getUrl() == null) {
                bitbucketProperties.setUrl(BitbucketUtils.inferEndpoint());
            }
            if (bitbucketProperties.getUser() == null) {
                bitbucketAuthentication = BitbucketUtils.inferAuthentication();
            } else {
                bitbucketAuthentication = new BitbucketAuthentication(bitbucketProperties.getUser() + ":" + bitbucketProperties.getPassword(),
                        AuthenticationType.Basic);
            }
        } else {
            bitbucketProperties = new BitbucketProperties();
            bitbucketProperties.setUrl(BitbucketUtils.inferEndpoint());
            bitbucketAuthentication = BitbucketUtils.inferAuthentication();
        }
        this.bitbucketAuthentication = bitbucketAuthentication;
        this.bitbucketApi = bitbucketApi;

    }

    /**
     * Create a BitbucketClient inferring endpoint and authentication from
     * environment and system properties.
     */

    public static BitbucketClient create(BitbucketProperties bitbucketProperties) {
        return new BitbucketClient(bitbucketProperties, bitbucketApi(bitbucketProperties, null));
    }

    public static BitbucketClient create(BitbucketProperties bitbucketProperties, WebClient webClient) {
        return new BitbucketClient(bitbucketProperties, bitbucketApi(bitbucketProperties, webClient));
    }

    private static BitbucketApi bitbucketApi(BitbucketProperties bitbucketProperties, WebClient webClient) {

        return new BitbucketApi() {

            private final HttpServiceProxyFactory httpServiceProxyFactory = buildHttpServiceProxyFactory(bitbucketProperties, webClient, TEMPLATE_AND_VALUES);

            @Override
            public AdminApi adminApi() {
                return httpServiceProxyFactory.createClient(AdminApi.class);
            }

            @Override
            public BranchApi branchApi() {
                return httpServiceProxyFactory.createClient(BranchApi.class);
            }

            @Override
            public BuildStatusApi buildStatusApi() {
                return httpServiceProxyFactory.createClient(BuildStatusApi.class);
            }

            @Override
            public CommentsApi commentsApi() {
                return httpServiceProxyFactory.createClient(CommentsApi.class);
            }

            @Override
            public CommitsApi commitsApi() {
                return httpServiceProxyFactory.createClient(CommitsApi.class);
            }

            @Override
            public DefaultReviewersApi defaultReviewersApi() {
                return httpServiceProxyFactory.createClient(DefaultReviewersApi.class);
            }

            @Override
            public FileApi fileApi() {
                return buildHttpServiceProxyFactory(bitbucketProperties, webClient, NONE).createClient(FileApi.class);
            }

            @Override
            public HookApi hookApi() {
                return httpServiceProxyFactory.createClient(HookApi.class);
            }

            @Override
            public WebHookApi webHookApi() {
                return httpServiceProxyFactory.createClient(WebHookApi.class);
            }

            @Override
            public ProjectApi projectApi() {
                return httpServiceProxyFactory.createClient(ProjectApi.class);
            }

            @Override
            public PullRequestApi pullRequestApi() {
                return httpServiceProxyFactory.createClient(PullRequestApi.class);
            }

            @Override
            public RepositoryApi repositoryApi() {
                return httpServiceProxyFactory.createClient(RepositoryApi.class);
            }

            @Override
            public SyncApi syncApi() {
                return httpServiceProxyFactory.createClient(SyncApi.class);
            }

            @Override
            public SystemApi systemApi() {
                return httpServiceProxyFactory.createClient(SystemApi.class);
            }

            @Override
            public TagApi tagApi() {
                return httpServiceProxyFactory.createClient(TagApi.class);
            }

            @Override
            public InsightsApi insightsApi() {
                return httpServiceProxyFactory.createClient(InsightsApi.class);
            }

            @Override
            public KeysApi keysApi() {
                return httpServiceProxyFactory.createClient(KeysApi.class);
            }

            @Override
            public LabelsApi labelsApi() {
                return httpServiceProxyFactory.createClient(LabelsApi.class);
            }

            @Override
            public PostWebHookApi postWebHookApi() {
                return httpServiceProxyFactory.createClient(PostWebHookApi.class);
            }

            @Override
            public LikesApi likesApi() {
                return httpServiceProxyFactory.createClient(LikesApi.class);
            }

            @Override
            public void close() {
            }
        };
    }

    private static HttpServiceProxyFactory buildHttpServiceProxyFactory(BitbucketProperties bitbucketProperties, WebClient webClient,
                                                                        DefaultUriBuilderFactory.EncodingMode encodingMode) {
        BitbucketAuthenticationFilter bitbucketAuthenticationFilter = new BitbucketAuthenticationFilter(bitbucketProperties.bitbucketAuthentication());
        ScrubNullFromPathFilter scrubNullFromPathFilter = new ScrubNullFromPathFilter();
        if (webClient == null) {
            DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(bitbucketProperties.getUrl());
            factory.setEncodingMode(encodingMode);

            webClient = WebClient.builder()
                    .uriBuilderFactory(factory)
                    .filter(bitbucketAuthenticationFilter)
                    .filter(scrubNullFromPathFilter)
                    .filter(BitbucketErrorHandler.handler())
                    .build();
        }
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
    }

    public String authValue() {
        return this.bitbucketAuthentication.authValue();
    }


    public BitbucketApi api() {
        return this.bitbucketApi;
    }

    @Override
    public void close() throws IOException {
        if (this.api() != null) {
            this.api().close();
        }
    }

}
