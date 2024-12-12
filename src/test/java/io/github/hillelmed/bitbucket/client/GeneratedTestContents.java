package io.github.hillelmed.bitbucket.client;

import io.github.hillelmed.bitbucket.client.domain.project.*;
import io.github.hillelmed.bitbucket.client.domain.repository.*;

import java.util.*;

/**
 * Umbrella for all generated test contents.
 */
public class GeneratedTestContents {

    public final Project project;
    public final Repository repository;
    public final String emptyRepositoryName;
    public final List<String[]> projectRepoMapping = new ArrayList<>();

    public final boolean projectPreviouslyExists;

    /**
     * Default constructor for org.midast.bitbucket.client.starter.GeneratedTestContents.
     *
     * @param project                 previously created Project.
     * @param repository              previously created Repository.
     * @param emptyRepositoryName     previously created Repository with no contents.
     * @param projectPreviouslyExists whether the test suite created or user passed in.
     */
    public GeneratedTestContents(final Project project,
                                 final Repository repository,
                                 final String emptyRepositoryName,
                                 final boolean projectPreviouslyExists) {

        this.project = project;
        this.repository = repository;
        this.emptyRepositoryName = emptyRepositoryName;
        this.projectPreviouslyExists = projectPreviouslyExists;
    }

    public void addRepoForDeletion(final String project, final String repository) {
        final String[] mapping = {project, repository};
        projectRepoMapping.add(mapping);
    }
}
