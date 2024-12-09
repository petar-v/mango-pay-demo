package org.flutterbit.mangopay.demo;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.model.ProjectIdea;
import org.flutterbit.mangopay.demo.repository.ProjectIdeaRepository;
import org.slf4j.Logger;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.slf4j.LoggerFactory;

@Singleton
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final ProjectIdeaRepository projectIdeaRepository;
    private final CoolUserRepository coolUserRepository;
    
    public DataLoader(ProjectIdeaRepository projectIdeaRepository, CoolUserRepository coolUserRepository) {
        this.projectIdeaRepository = projectIdeaRepository;
        this.coolUserRepository = coolUserRepository;
    }

    @EventListener
    public void onStartup(ServerStartupEvent event) {
        if (coolUserRepository.count() == 0) {
            CoolUser user = new CoolUser("Some cool User");
            coolUserRepository.save(user);
        }
        if (projectIdeaRepository.count() == 0) {
            CoolUser user = coolUserRepository.findAll().get(0);
            LOGGER.info("Loading sample project ideas into the database.");
            
            ProjectIdea idea1 = new ProjectIdea("AI-powered To-do List", "An AI-based to-do list that auto-prioritizes tasks.", user);
            ProjectIdea idea2 = new ProjectIdea("Smart Home Dashboard", "A centralized dashboard for smart home devices.", user);

            projectIdeaRepository.save(idea1);
            projectIdeaRepository.save(idea2);
        }
    }
}
