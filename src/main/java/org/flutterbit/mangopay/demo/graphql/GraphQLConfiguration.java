package org.flutterbit.mangopay.demo.graphql;

import graphql.schema.idl.RuntimeWiring;
import io.micronaut.context.annotation.Factory;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Singleton;
import org.flutterbit.mangopay.demo.data.ProjectIdeaDataFetchers;

@Factory
@Secured(SecurityRule.IS_ANONYMOUS)
public class GraphQLConfiguration {

    private final ProjectIdeaDataFetchers projectIdeaDataFetchers;

    public GraphQLConfiguration(ProjectIdeaDataFetchers projectIdeaDataFetchers) {
        this.projectIdeaDataFetchers = projectIdeaDataFetchers;
    }

    @Singleton
    public RuntimeWiring runtimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("listIdeas", projectIdeaDataFetchers.listIdeasFetcher()))
                .build();
    }
}
