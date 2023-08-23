package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;


@Test(groups = "live", testName = "BranchApiLiveTest", singleThreaded = true)
public class BranchApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String branchName = TestUtilities.randomStringLettersOnly();
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private String emptyRepoKey;
    private String defaultBranchId;
    private String commitHash;
    private Long branchPermissionId;
    private BranchModelConfiguration branchModelConfiguration;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
        this.emptyRepoKey = generatedTestContents.emptyRepositoryName;

        final Branch branch = api().getDefault(projectKey, repoKey).getBody();
        assertThat(branch).isNotNull();
        defaultBranchId = branch.getId();
        commitHash = branch.getLatestCommit();
    }

    @Test
    public void testCreateBranchInEmptyRepository() {
        final CreateBranch createBranch = new CreateBranch(branchName, commitHash, null);
        try {
            final Branch branch = api().create(projectKey, emptyRepoKey, createBranch).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors().get(0).getExceptionName()).isEqualTo("com.atlassian.bitbucket.repository.IllegalRepositoryStateException");
        }
    }

    @Test
    public void testCreateBranch() {
        final CreateBranch createBranch = new CreateBranch(branchName, commitHash, null);
        final Branch branch = api().create(projectKey, repoKey, createBranch).getBody();
        assertThat(branch).isNotNull();
        assertThat(branch.getId().endsWith(branchName)).isTrue();
        assertThat(commitHash.equalsIgnoreCase(branch.getLatestChangeset())).isTrue();
    }

    @Test(dependsOnMethods = "testCreateBranch")
    public void testListBranches() {
        final BranchPage branch = api().list(projectKey, repoKey, null, null, null, null, null, 1).getBody();
        assertThat(branch).isNotNull();
        assertThat(branch.getValues().size() > 0).isTrue();
    }

    @Test(dependsOnMethods = "testCreateBranch")
    public void testGetBranchInfo() {
        final BranchPage branch = api().info(projectKey, repoKey, commitHash).getBody();
        assertThat(branch).isNotNull();
        assertThat(branch.getValues().size() > 0).isTrue();
    }

    @Test(dependsOnMethods = "testGetBranchInfo")
    public void testGetBranchModel() {
        final BranchModel branchModel = api().model(projectKey, repoKey).getBody();
        assertThat(branchModel).isNotNull();
    }

    @Test(dependsOnMethods = "testGetBranchModel")
    public void testUpdateDefaultBranch() {
        final ResponseEntity<Void> success = api().updateDefault(projectKey, repoKey, new UpdateDefaultBranch("refs/heads/" + branchName));
        assertThat(success).isNotNull();
        //assertThat(success.getValue()).isTrue();
    }

    @Test(dependsOnMethods = "testUpdateDefaultBranch")
    public void testGetNewDefaultBranch() {
        final Branch branch = api().getDefault(projectKey, repoKey).getBody();
        assertThat(branch).isNotNull();
        assertThat(branch.getId()).isNotNull();
    }

    @Test(dependsOnMethods = "testGetNewDefaultBranch")
    public void testCreateBranchPermission() {
        final List<String> groupPermission = new ArrayList<>();
        groupPermission.add(defaultBitbucketGroup);

        final List<BranchRestriction> restrictions = new ArrayList<>();
        restrictions.add(new BranchRestriction(null, BranchRestrictionEnumType.FAST_FORWARD_ONLY,
                new Matcher(Matcher.MatcherId.RELEASE, true), new ArrayList<>(), groupPermission, null));
        restrictions.add(new BranchRestriction(null, BranchRestrictionEnumType.FAST_FORWARD_ONLY,
                new Matcher(Matcher.MatcherId.DEVELOPMENT, true), new ArrayList<>(), groupPermission, null));
        restrictions.add(new BranchRestriction(null, BranchRestrictionEnumType.FAST_FORWARD_ONLY,
                new Matcher(Matcher.MatcherId.MASTER, true), new ArrayList<>(), groupPermission, null));

        List<BranchRestriction> success = null;
        try {
            success = api().createBranchRestriction(projectKey, repoKey, restrictions).getBody();
        } catch (BitbucketAppException e) {
            e.errors().forEach(bitbucketError -> System.out.println(bitbucketError.getMessage()));
        }
        assertThat(success).isNotNull();
        assertThat(success.isEmpty()).isFalse();
    }

    @Test(dependsOnMethods = "testCreateBranchPermission")
    public void testListBranchPermission() {
        final BranchRestrictionPage branchPermissionPage = api().listBranchRestriction(projectKey, repoKey, null, 1).getBody();
        assertThat(branchPermissionPage).isNotNull();
        assertThat(!branchPermissionPage.getValues().isEmpty()).isTrue();
        branchPermissionId = branchPermissionPage.getValues().get(0).getId();
    }

    @Test(dependsOnMethods = "testListBranchPermission")
    public void testDeleteBranchPermission() {
        if (branchPermissionId != null) {
            final ResponseEntity<Void> success = api().deleteBranchRestriction(projectKey, repoKey, branchPermissionId);
            assertThat(success).isNotNull();
            //assertThat(success.getValue()).isTrue();
        } else {
            fail("branchPermissionId is null");
        }
    }

    @Test(dependsOnMethods = {"testListBranches"})
    public void testGetBranchModelConfiguration() {
        branchModelConfiguration = api().getModelConfiguration(projectKey, repoKey).getBody();
        checkDefaultBranchConfiguration();
    }

    @Test(dependsOnMethods = {"testGetBranchModelConfiguration"})
    public void testUpdateBranchModelConfiguration() {
        final List<Type> types = new ArrayList<>();
        types.add(new Type(Type.TypeId.BUGFIX, null, "bug/", false));
        types.add(new Type(Type.TypeId.HOTFIX, null, "hot/", true));
        types.add(new Type(Type.TypeId.RELEASE, null, "rel/", true));
        types.add(new Type(Type.TypeId.FEATURE, null, "fea/", true));
        final BranchConfiguration branchConfig = branchModelConfiguration.getDevelopment();
        final CreateBranchModelConfiguration conf = new CreateBranchModelConfiguration(branchConfig, null, types);

        final BranchModelConfiguration bmc = api().updateModelConfiguration(projectKey, repoKey, conf).getBody();
        assertThat(bmc).isNotNull();
        assertThat(bmc.getProduction()).isNull();
        assertThat(bmc.getTypes().size()).isEqualTo(4);
        for (final Type type : bmc.getTypes()) {
            switch (type.getId()) {
                case BUGFIX -> {
                    assertThat(type.getPrefix()).isEqualTo("bug/");
                    assertThat(type.isEnabled()).isFalse();
                }
                case HOTFIX -> {
                    assertThat(type.getPrefix()).isEqualTo("hot/");
                    assertThat(type.isEnabled()).isTrue();
                }
                case RELEASE -> {
                    assertThat(type.getPrefix()).isEqualTo("rel/");
                    assertThat(type.isEnabled()).isTrue();
                }
                case FEATURE -> {
                    assertThat(type.getPrefix()).isEqualTo("fea/");
                    assertThat(type.isEnabled()).isTrue();
                }
                default -> {
                }
            }
        }
    }

    @Test(dependsOnMethods = {"testUpdateBranchModelConfiguration"})
    public void testDeleteBranchModelConfiguration() {
        final ResponseEntity<Void> success = api().deleteModelConfiguration(projectKey, repoKey);
        assertThat(success).isNotNull();
        //assertThat(success.getValue()).isTrue();
    }

    @Test(dependsOnMethods = {"testListBranches"})
    public void testGetBranchModelConfigurationOnError() {
        try {
            final BranchModelConfiguration configuration = api().getModelConfiguration(projectKey, TestUtilities.randomString()).getBody();
            assertThat(configuration).isNull();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testCreateBranch")
    public void testGetBranchInfoOnError() {
        try {
            final BranchPage branch = api().info(projectKey, repoKey, TestUtilities.randomString()).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    private void checkDefaultBranchConfiguration() {
        assertThat(branchModelConfiguration).isNotNull();
        assertThat(branchModelConfiguration.getProduction()).isNull();
        assertThat(branchModelConfiguration.getTypes().size()).isEqualTo(4);
        for (final Type type : branchModelConfiguration.getTypes()) {
            switch (type.getId()) {
                case BUGFIX -> assertThat(type.getPrefix()).isEqualTo("bugfix/");
                case HOTFIX -> assertThat(type.getPrefix()).isEqualTo("hotfix/");
                case FEATURE -> assertThat(type.getPrefix()).isEqualTo("feature/");
                case RELEASE -> assertThat(type.getPrefix()).isEqualTo("release/");
                default -> {
                }
            }
            assertThat(type.isEnabled()).isTrue();
        }
    }

    @AfterClass
    public void fin() {
        try {

            final ResponseEntity<Void> updateStatus = api().updateDefault(projectKey, repoKey, new UpdateDefaultBranch(defaultBranchId));
            assertThat(updateStatus).isNotNull();


            final ResponseEntity<Void> deleteStatus = api().delete(projectKey, repoKey, new DeleteBranch("refs/heads/" + branchName));
            assertThat(deleteStatus).isNotNull();
//            assertThat(deleteStatus.getValue()).isTrue();

            if (branchModelConfiguration != null) {
                branchModelConfiguration = api().updateModelConfiguration(projectKey, repoKey,
                        new CreateBranchModelConfiguration(branchModelConfiguration)).getBody();
                checkDefaultBranchConfiguration();
            }
        } finally {
            TestUtilities.terminateGeneratedTestContents(this.bitbucketClient.api(), generatedTestContents);
        }
    }

    private BranchApi api() {
        return this.bitbucketClient.api().branchApi();
    }
}
