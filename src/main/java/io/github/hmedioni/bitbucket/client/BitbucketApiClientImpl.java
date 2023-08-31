package io.github.hmedioni.bitbucket.client;

import io.github.hmedioni.bitbucket.client.config.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.filters.*;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.*;
import org.springframework.web.service.invoker.*;
import org.springframework.web.util.*;

import java.util.*;

import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.NONE;
import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES;

public class BitbucketApiClientImpl implements BitbucketApi {

    private final BitbucketProperties bitbucketProperties;
    private final WebClient webClient;
    private final HttpServiceProxyFactory httpServiceProxyFactory;
    private final Map<Class<?>, Object> singletons;

    public BitbucketApiClientImpl(BitbucketProperties bitbucketProperties, WebClient webClient) {
        this.bitbucketProperties = bitbucketProperties;
        this.webClient = webClient;
        this.httpServiceProxyFactory = buildHttpServiceProxyFactory(bitbucketProperties, webClient, TEMPLATE_AND_VALUES);
        this.singletons = Collections.synchronizedMap(new HashMap<>());
    }

    private synchronized <T> T getSingleton(Class<T> klass, Object o) {
        return klass.cast(singletons.computeIfAbsent(klass, aClass -> o));
    }

    private synchronized <T> T getSingleton(Class<T> klass) {
        return klass.cast(singletons.computeIfAbsent(klass, httpServiceProxyFactory::createClient));
    }


    @Override
    public AdminApi adminApi() {
        return getSingleton(AdminApi.class);
    }

    @Override
    public BranchApi branchApi() {
        return getSingleton(BranchApi.class);
    }

    @Override
    public BuildStatusApi buildStatusApi() {
        return getSingleton(BuildStatusApi.class);
    }

    @Override
    public CommentsApi commentsApi() {
        return getSingleton(CommentsApi.class);
    }

    @Override
    public CommitsApi commitsApi() {
        return getSingleton(CommitsApi.class);
    }

    @Override
    public DefaultReviewersApi defaultReviewersApi() {
        return getSingleton(DefaultReviewersApi.class);
    }

    @Override
    public FileApi fileApi() {
        return getSingleton(FileApi.class, buildHttpServiceProxyFactory(bitbucketProperties, webClient, NONE).createClient(FileApi.class));
    }

    @Override
    public HookApi hookApi() {
        return getSingleton(HookApi.class);
    }

    @Override
    public WebHookApi webHookApi() {
        return getSingleton(WebHookApi.class);
    }

    @Override
    public ProjectApi projectApi() {
        return getSingleton(ProjectApi.class);
    }

    @Override
    public PullRequestApi pullRequestApi() {
        return getSingleton(PullRequestApi.class);
    }

    @Override
    public RepositoryApi repositoryApi() {
        return getSingleton(RepositoryApi.class);
    }

    @Override
    public SyncApi syncApi() {
        return getSingleton(SyncApi.class);
    }

    @Override
    public SystemApi systemApi() {
        return getSingleton(SystemApi.class);
    }

    @Override
    public TagApi tagApi() {
        return getSingleton(TagApi.class);
    }

    @Override
    public InsightsApi insightsApi() {
        return getSingleton(InsightsApi.class);
    }

    @Override
    public KeysApi keysApi() {
        return getSingleton(KeysApi.class);
    }

    @Override
    public LabelsApi labelsApi() {
        return getSingleton(LabelsApi.class);
    }

    @Override
    public PostWebHookApi postWebHookApi() {
        return getSingleton(PostWebHookApi.class);
    }

    @Override
    public LikesApi likesApi() {
        return getSingleton(LikesApi.class);
    }

    @Override
    public void close() {
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


}
