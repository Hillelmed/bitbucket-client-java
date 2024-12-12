package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.domain.defaultreviewers.Condition;
import io.github.hillelmed.bitbucket.client.domain.defaultreviewers.*;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "DefaultReviewersApiLiveTest", singleThreaded = true)
public class DefaultReviewersApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String defaultReviewersOnNewRepoMethod = "testListDefaultReviewersOnNewRepo";
    private final String testCreateConditionMethod = "testCreateCondition";

    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private Long conditionId;
    private User user;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
        this.user = TestUtilities.getDefaultUser(this.bitbucketClient.getBitbucketAuthentication(), this.bitbucketClient.api());
    }

    @Test
    public void testListDefaultReviewersOnNewRepo() {
        final List<Condition> conditionList = api().listConditions(projectKey, repoKey).getBody();
        assertThat(conditionList).isEmpty();
    }


    @Test(dependsOnMethods = {defaultReviewersOnNewRepoMethod})
    public void testCreateCondition() {
        final Long requiredApprover = 1L;
        final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
        final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
        final List<User> listUser = new ArrayList<>();
        listUser.add(user);
        final CreateCondition condition = new CreateCondition(null, matcherSrc,
                matcherDst, listUser, requiredApprover);

        final Condition returnCondition = api().createCondition(projectKey, repoKey, condition).getBody();
        conditionId = returnCondition.getId();
        validCondition(returnCondition, requiredApprover, Matcher.MatcherId.ANY_REF, Matcher.MatcherId.ANY_REF);
    }

    @Test(dependsOnMethods = {defaultReviewersOnNewRepoMethod})
    public void testCreateConditionOnError() {
        final Long requiredApprover = 1L;
        final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
        final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
        final List<User> listUser = new ArrayList<>();
        listUser.add(user);
        final CreateCondition condition = new CreateCondition(null,
                matcherSrc,
                matcherDst,
                listUser,
                requiredApprover);
        try {
            final Condition returnCondition = api().createCondition(projectKey, "1234", condition).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();

        }
    }

    @Test(dependsOnMethods = {defaultReviewersOnNewRepoMethod})
    public void testCreateConditionMatcherDifferent() {
        final Long requiredApprover = 1L;
        final Matcher matcherSrc = new Matcher(Matcher.MatcherId.MASTER, true);
        final Matcher matcherDst = new Matcher(Matcher.MatcherId.DEVELOPMENT, true);
        final List<User> listUser = new ArrayList<>();
        listUser.add(user);
        final CreateCondition condition = new CreateCondition(null, matcherSrc,
                matcherDst, listUser, requiredApprover);

        final Condition returnCondition = api().createCondition(projectKey, repoKey, condition).getBody();
        validCondition(returnCondition, requiredApprover, Matcher.MatcherId.MASTER, Matcher.MatcherId.DEVELOPMENT);
    }

    @Test(dependsOnMethods = {testCreateConditionMethod})
    public void testUpdateCondition() {
        final Long requiredApprover = 0L;
        final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
        final Matcher matcherDst = new Matcher(Matcher.MatcherId.DEVELOPMENT, true);
        final List<User> listUser = new ArrayList<>();
        listUser.add(user);
        final CreateCondition condition = new CreateCondition(conditionId,
                matcherSrc,
                matcherDst,
                listUser,
                requiredApprover);

        final Condition returnCondition = api().updateCondition(projectKey, repoKey, conditionId, condition).getBody();
        validCondition(returnCondition, requiredApprover, Matcher.MatcherId.ANY_REF, Matcher.MatcherId.DEVELOPMENT);
        assertThat(returnCondition.getId()).isEqualTo(conditionId);
    }

    @Test(dependsOnMethods = {"testUpdateCondition", testCreateConditionMethod, "testCreateConditionMatcherDifferent"})
    public void testListConditions() {
        final List<Condition> listCondition = api().listConditions(projectKey, repoKey).getBody();
        assertThat(listCondition.size()).isEqualTo(2);
        for (final Condition condition : listCondition) {
            if (condition.getId().equals(conditionId)) {
                validCondition(condition, 0L, Matcher.MatcherId.ANY_REF, Matcher.MatcherId.DEVELOPMENT);
            } else {
                validCondition(condition, 1L, Matcher.MatcherId.MASTER, Matcher.MatcherId.DEVELOPMENT);
            }
        }
    }

    @Test(dependsOnMethods = {defaultReviewersOnNewRepoMethod, testCreateConditionMethod, "testUpdateCondition",
            "testCreateConditionMatcherDifferent", "testListConditions"})
    public void testDeleteCondition() {
        final ResponseEntity<Void> success = api().deleteCondition(projectKey, repoKey, conditionId);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test()
    public void testDeleteConditionOnError() {
        try {
            final ResponseEntity<Void> success = api().deleteCondition(projectKey, repoKey, -1);

        } catch (BitbucketAppException e) {
            assertThat(e).isNotNull();
            assertThat(e.code().is4xxClientError()).isTrue();
        }

    }

    @Test(dependsOnMethods = {defaultReviewersOnNewRepoMethod, testCreateConditionMethod, "testUpdateCondition",
            "testCreateConditionMatcherDifferent", "testListConditions", "testDeleteCondition"})
    public void testListConditionsAfterDelete() {
        final List<Condition> listCondition = api().listConditions(projectKey, repoKey).getBody();
        assertThat(listCondition).hasSize(1);
        for (final Condition condition : listCondition) {
            assertThat(condition.getId()).isNotEqualTo(conditionId);
            validCondition(condition, 1L, Matcher.MatcherId.MASTER, Matcher.MatcherId.DEVELOPMENT);
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(this.bitbucketClient.api(), generatedTestContents);
    }

    private DefaultReviewersApi api() {
        return this.bitbucketClient.api().defaultReviewersApi();
    }

    private void validCondition(final Condition returnValue,
                                final Long requiredApprover,
                                final Matcher.MatcherId matcherSrc,
                                final Matcher.MatcherId matcherDst) {


        // fix for Bitbucket server 4.x where scope is not defined
        if (returnValue.getScope() != null) {
            assertThat(returnValue.getScope().getType()).isEqualTo(Scope.ScopeType.REPOSITORY);
        }

        assertThat(returnValue.getId()).isNotNull();
        assertThat(returnValue.getRequiredApprovals()).isEqualTo(requiredApprover);
        assertThat(returnValue.getReviewers().size()).isEqualTo(1);
        assertThat(returnValue.getSourceRefMatcher().getId()).isEqualTo(matcherSrc.getId());
        assertThat(returnValue.getTargetRefMatcher().getId()).isEqualTo(matcherDst.getId());
    }
}
