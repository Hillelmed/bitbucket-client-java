package io.github.hmedioni.bitbucket.client;

import io.github.hmedioni.bitbucket.client.auth.*;
import io.github.hmedioni.bitbucket.client.config.*;
import io.github.hmedioni.bitbucket.client.domain.admin.*;
import io.github.hmedioni.bitbucket.client.domain.project.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.apache.commons.io.*;
import org.springframework.http.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.Path;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class TestUtilities extends BitbucketUtils {

    public static final String TEST_CREDENTIALS_SYSTEM_PROPERTY = "test.bitbucket.rest.credentials";
    public static final String TEST_CREDENTIALS_ENVIRONMENT_VARIABLE = TEST_CREDENTIALS_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String TEST_TOKEN_SYSTEM_PROPERTY = "test.bitbucket.rest.token";
    public static final String TEST_TOKEN_ENVIRONMENT_VARIABLE = TEST_TOKEN_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    private static final String GIT_COMMAND = "git";
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static User defaultUser;
    private static File gitDirectory;

    private TestUtilities() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }

    /**
     * Get the default User from the passed credential String. Once user
     * is found we will cache for later usage.
     *
     * @param api  Bitbucket api object.
     * @param auth the BitbucketAuthentication instance.
     * @return User or null if user can't be inferred.
     */
    public static synchronized User getDefaultUser(final BitbucketAuthentication auth, final BitbucketApi api) {
        if (defaultUser == null) {
            assertThat(auth).isNotNull();
            assertThat(api).isNotNull();

            if (auth.authType() == AuthenticationType.Basic) {
                final String username = new String(Base64.getDecoder().decode(auth.authValue())).split(":")[0];
                final UserPage userPage = api.adminApi().listUsers(username, null, null).getBody();
                assertThat(userPage).isNotNull();
                assertThat(userPage.getSize() > 0).isTrue();
                for (final User user : userPage.getValues()) {
                    if (username.equalsIgnoreCase(user.getName())) {
                        defaultUser = user;
                        break;
                    }
                }
                assertThat(defaultUser).isNotNull();
            }
        }

        return defaultUser;
    }

    /**
     * Execute `args` at the `workingDir`.
     *
     * @param args       list of arguments to pass to Process.
     * @param workingDir directory to execute Process within.
     * @return possible output of Process.
     * @throws Exception if Process could not be successfully executed.
     */
    public static String executionToString(final List<String> args, final Path workingDir) throws Exception {
        assertThat(args).isNotNull().isNotEmpty();
        assertThat(workingDir).isNotNull();
        assertThat(workingDir.toFile().isDirectory()).isTrue();

        final Process process = new ProcessBuilder(args)
                .redirectErrorStream(true)
                .directory(workingDir.toFile())
                .start();

        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

    }

    /**
     * Generate a dummy file at the `baseDir` location.
     *
     * @param baseDir directory to generate the file under.
     * @return Path pointing at generated file.
     * @throws Exception if file could not be written.
     */
    public static Path initGeneratedFile(final Path baseDir) throws Exception {
        assertThat(baseDir).isNotNull();
        assertThat(baseDir.toFile().isDirectory()).isTrue();

        final String randomName = randomString();

        final List<String> lines = Collections.singletonList(randomName);
        final Path file = Paths.get(new File(baseDir.toFile(), randomName + ".txt").toURI());
        return Files.write(file, lines, StandardCharsets.UTF_8);
    }

    /**
     * Initialize live test contents.
     *
     * @return org.midast.bitbucket.client.starter.GeneratedTestContents to use.
     */
    public static synchronized GeneratedTestContents initGeneratedTestContents(final BitbucketClient client, final BitbucketProperties bitbucketProperties) {
        final BitbucketApi api = client.api();
        assertThat(api).isNotNull();

        // get possibly existing projectKey that user passed in
        String projectKey = System.getProperty("test.bitbucket.project");
        if (projectKey == null) {
            projectKey = randomStringLettersOnly();
        }

        // create test project if one does not already exist
        boolean projectPreviouslyExists = true;
        Project project;
        try {
            project = api.projectApi().get(projectKey).getBody();
            assertThat(project).isNotNull();
        } catch (BitbucketAppException e) {
            projectPreviouslyExists = false;
            final CreateProject createProject = new CreateProject(projectKey, null, null, null);
            project = api.projectApi().create(createProject).getBody();
            assertThat(project).isNotNull();
        }

        // create test repo that remains empty
        final String emptyRepoKey = randomStringLettersOnly();
        final CreateRepository createEmptyRepository = new CreateRepository(emptyRepoKey, null, null, true);
        final Repository emptyRepository = api.repositoryApi().create(projectKey, createEmptyRepository).getBody();
        assertThat(emptyRepository).isNotNull();

        // create test repo
        final String repoKey = randomStringLettersOnly();
        final CreateRepository createRepository = new CreateRepository(repoKey, null, null, true);
        final Repository repository = api.repositoryApi().create(projectKey, createRepository).getBody();
        assertThat(repository).isNotNull();
        Path dir = null;
        try {
            dir = Files.createTempDirectory("temp").toAbsolutePath();
        } catch (IOException e) {
        }
        final Path testDir;
        if (dir != null) {
            testDir = dir;
        } else {
            testDir = Paths.get(System.getProperty("test.bitbucket.basedir"));
        }
        assertThat(testDir.toFile().exists()).isTrue();
        assertThat(testDir.toFile().isDirectory()).isTrue();

        final String randomName = randomString();
        gitDirectory = new File(testDir.toFile(), randomName);
        assertThat(gitDirectory.mkdirs()).isTrue();

        try {
            final String foundCredential = getDefaultUser(client.getBitbucketAuthentication(), api).getSlug();
            BitbucketAuthentication auth = client.getBitbucketAuthentication();
            final String username = new String(Base64.getDecoder().decode(auth.authValue())).split(":")[0];
            final String password = new String(Base64.getDecoder().decode(auth.authValue())).split(":")[1];

            final URL endpointURL = new URL(bitbucketProperties.getUrl());
            final int index = endpointURL.toString().indexOf(endpointURL.getHost());
            final String preCredentialPart = endpointURL.toString().substring(0, index);
            final String postCredentialPart = endpointURL.toString().substring(index);

            final String generatedEndpoint = preCredentialPart
                    + username + ":" + password + "@"
                    + postCredentialPart + "/scm/"
                    + projectKey.toLowerCase() + "/"
                    + repoKey.toLowerCase() + ".git";

            generateGitContentsAndPush(generatedEndpoint);

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        final GeneratedTestContents gtc = new GeneratedTestContents(project, repository, emptyRepository.getName(), projectPreviouslyExists);
        gtc.addRepoForDeletion(projectKey, emptyRepoKey);
        gtc.addRepoForDeletion(projectKey, repoKey);
        return gtc;
    }

    /**
     * Initialize git repository and add some randomly generated files.
     *
     * @param gitRepoURL git repository URL with embedded credentials.
     * @throws Exception if git repository could not be created or files added.
     */
    public static void generateGitContentsAndPush(final String gitRepoURL) throws Exception {

        // 1.) initialize git repository
        final String initGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "init"), gitDirectory.toPath());
        System.out.println("git-init: " + initGit.trim());

        // 2.) create some random files and commit them
        for (int i = 0; i < 3; i++) {
            Path genFile = initGeneratedFile(gitDirectory.toPath());
            String addGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "add", genFile.toFile().getPath()), gitDirectory.toPath());
            System.out.println("git-add-1: " + addGit.trim());
            String commitGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "commit", "-m", "added"), gitDirectory.toPath());
            System.out.println("git-commit-1: " + commitGit.trim());

            // edit file again and create another commit
            genFile = Files.write(genFile, Collections.singletonList(randomString()), StandardCharsets.UTF_8);
            addGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "add", genFile.toFile().getPath()), gitDirectory.toPath());
            System.out.println("git-add-2: " + addGit.trim());
            commitGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "commit", "-m", "added"), gitDirectory.toPath());
            System.out.println("git-commit-2: " + commitGit.trim());
            final String tagGit = TestUtilities
                    .executionToString(Arrays
                            .asList(GIT_COMMAND, "tag", "-a", "v0.0." + (i + 1), "-m", "\"generated version\""), gitDirectory.toPath());
            System.out.println("git-tag-commit: " + tagGit.trim());
        }

        // 3.) push changes to remote repository
        final String pushGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND,
                "push",
                "--set-upstream",
                gitRepoURL,
                "master"), gitDirectory.toPath());
        System.out.println("git-push: " + pushGit);

        // 4.) create branch
        final String generatedBranchName = randomString();
        final String branchGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND,
                        "checkout", "-b",
                        generatedBranchName),
                gitDirectory.toPath());
        System.out.println("git-branch: " + branchGit.trim());


        // 5.) generate random file for new branch
        final Path genFile = initGeneratedFile(gitDirectory.toPath());
        final String addGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "add", genFile.toFile().getPath()), gitDirectory.toPath());
        System.out.println("git-branch-add: " + addGit.trim());
        final String commitGit = TestUtilities.executionToString(Arrays.asList(GIT_COMMAND, "commit", "-m", "added"), gitDirectory.toPath());
        System.out.println("git-branch-commit: " + commitGit.trim());

        // 6.) push branch
        final List<String> args = Arrays.asList(GIT_COMMAND, "push", "--tags", "-u", gitRepoURL, generatedBranchName);
        final String pushBranchGit = TestUtilities.executionToString(args, gitDirectory.toPath());
        System.out.println("git-branch-push: " + pushBranchGit);
    }

    /**
     * Terminate live test contents.
     *
     * @param api                   BitbucketApi object
     * @param generatedTestContents to terminate.
     */
    public static synchronized void terminateGeneratedTestContents(final BitbucketApi api,
                                                                   final GeneratedTestContents generatedTestContents) {
        assertThat(api).isNotNull();
        assertThat(generatedTestContents).isNotNull();

        final Project project = generatedTestContents.project;
        final Repository repository = generatedTestContents.repository;

        // delete main repository
        final ResponseEntity<Void> success = api.repositoryApi().delete(project.getKey(), repository.getName());
        assertThat(success).isNotNull();
//        assertThat(success.getValue()).isTrue();

        // delete all attached repos
        for (final String[] mapping : generatedTestContents.projectRepoMapping) {

            final String projectKey = mapping[0];
            final String repoKey = mapping[1];
            try {
                final ResponseEntity<Void> successInner = api.repositoryApi().delete(projectKey, repoKey);
                assertThat(successInner).isNotNull();
//                assertThat(successInner.getValue()).isTrue();
            } catch (Exception e) {
                if (e instanceof BitbucketAppException) {
                    throw new RuntimeException("Failed deleting repo: " + ((BitbucketAppException) e).errors().get(0).getContext());
                }
            }
        }

        // delete project
        if (!generatedTestContents.projectPreviouslyExists) {
            final ResponseEntity<Void> deleteStatus = api.projectApi().delete(project.getKey());
            assertThat(deleteStatus).isNotNull();
//            assertThat(deleteStatus.getValue()).isTrue();
        }

        // delete the local git directory
        try {
            FileUtils.deleteDirectory(new File(String.valueOf(gitDirectory)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Generate a random String with letters only.
     *
     * @return random String.
     */
    public static String randomStringLettersOnly() {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 10; i++) {
            final char randomChar = CHARS[random.nextInt(CHARS.length)];
            sb.append(randomChar);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Generate a random String with numbers and letters.
     *
     * @return random String.
     */
    public static String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Find credentials (Basic, Bearer, or Anonymous) from system/environment.
     *
     * @return BitbucketCredentials
     */
    public static BitbucketAuthentication inferTestAuthentication() {

        // 1.) Check for "Basic" auth credentials.
        final BitbucketAuthentication.Builder inferAuth = BitbucketAuthentication.builder();
        String authValue = BitbucketUtils
                .retrieveExternalValue(TEST_CREDENTIALS_SYSTEM_PROPERTY,
                        TEST_CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
        } else {

            // 2.) Check for "Bearer" auth token.
            authValue = BitbucketUtils
                    .retrieveExternalValue(TEST_TOKEN_SYSTEM_PROPERTY,
                            TEST_TOKEN_ENVIRONMENT_VARIABLE);
            if (authValue != null) {
                inferAuth.credentials(authValue);
            }
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth.build();
    }
}
