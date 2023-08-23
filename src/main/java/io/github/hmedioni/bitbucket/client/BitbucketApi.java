package io.github.hmedioni.bitbucket.client;

import io.github.hmedioni.bitbucket.client.features.blocking.*;

import java.io.*;

public interface BitbucketApi extends Closeable {

    AdminApi adminApi();


    BranchApi branchApi();


    BuildStatusApi buildStatusApi();


    CommentsApi commentsApi();


    CommitsApi commitsApi();


    DefaultReviewersApi defaultReviewersApi();


    FileApi fileApi();


    HookApi hookApi();


    WebHookApi webHookApi();


    ProjectApi projectApi();


    PullRequestApi pullRequestApi();


    RepositoryApi repositoryApi();


    SyncApi syncApi();


    SystemApi systemApi();


    TagApi tagApi();


    InsightsApi insightsApi();


    KeysApi keysApi();


    LabelsApi labelsApi();


    PostWebHookApi postWebHookApi();


    LikesApi likesApi();
}
