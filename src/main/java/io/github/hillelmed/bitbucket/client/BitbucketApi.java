package io.github.hillelmed.bitbucket.client;

import io.github.hillelmed.bitbucket.client.features.blocking.*;

import java.io.Closeable;

/**
 * The interface Bitbucket api.
 */
public interface BitbucketApi extends Closeable {

    /**
     * Admin api admin api.
     *
     * @return the admin api
     */
    AdminApi adminApi();


    /**
     * Branch api branch api.
     *
     * @return the branch api
     */
    BranchApi branchApi();


    /**
     * Build status api build status api.
     *
     * @return the build status api
     */
    BuildStatusApi buildStatusApi();


    /**
     * Comments api comments api.
     *
     * @return the comments api
     */
    CommentsApi commentsApi();


    /**
     * Commits api commits api.
     *
     * @return the commits api
     */
    CommitsApi commitsApi();


    /**
     * Default reviewers api default reviewers api.
     *
     * @return the default reviewers api
     */
    DefaultReviewersApi defaultReviewersApi();


    /**
     * File api file api.
     *
     * @return the file api
     */
    FileApi fileApi();


    /**
     * Hook api hook api.
     *
     * @return the hook api
     */
    HookApi hookApi();


    /**
     * Web hook api web hook api.
     *
     * @return the web hook api
     */
    WebHookApi webHookApi();


    /**
     * Project api project api.
     *
     * @return the project api
     */
    ProjectApi projectApi();


    /**
     * Pull request api pull request api.
     *
     * @return the pull request api
     */
    PullRequestApi pullRequestApi();


    /**
     * Repository api repository api.
     *
     * @return the repository api
     */
    RepositoryApi repositoryApi();


    /**
     * Sync api sync api.
     *
     * @return the sync api
     */
    SyncApi syncApi();


    /**
     * System api system api.
     *
     * @return the system api
     */
    SystemApi systemApi();


    /**
     * Tag api tag api.
     *
     * @return the tag api
     */
    TagApi tagApi();


    /**
     * Insights api insights api.
     *
     * @return the insights api
     */
    InsightsApi insightsApi();


    /**
     * Keys api keys api.
     *
     * @return the keys api
     */
    KeysApi keysApi();


    /**
     * Labels api labels api.
     *
     * @return the labels api
     */
    LabelsApi labelsApi();


    /**
     * Post web hook api post web hook api.
     *
     * @return the post web hook api
     */
    PostWebHookApi postWebHookApi();


    /**
     * Likes api likes api.
     *
     * @return the likes api
     */
    LikesApi likesApi();
}
