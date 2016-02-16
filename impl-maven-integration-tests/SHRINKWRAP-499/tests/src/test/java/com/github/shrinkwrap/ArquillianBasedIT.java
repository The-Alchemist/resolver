package com.github.shrinkwrap;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Very simple Arquillian setup
 * 
 * 
 */
@RunWith(Arquillian.class)
public class ArquillianBasedIT {
    
    private Logger logger = Logger.getLogger(getClass());

    
    @Deployment
    public static Archive<?> deploy() throws IOException {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
             // enables CDI
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        
        /*
         *  gives weird Arquillian/shrinkwrap error
         *  
         */
         addDependencyToArchive("org.apache.commons:commons-math3", war, false);

        return war;
    }

    static void addDependencyToArchive(String depCanonicalForm, WebArchive war, boolean withTransitivity) {
        MavenStrategyStage resolver = Maven.configureResolver().workOffline(true).loadPomFromFile("pom.xml").resolve(depCanonicalForm);
        final File[] jars;
        if(withTransitivity) {
            jars = resolver.withTransitivity().asFile();
        }
        else
        {
            jars = resolver.withoutTransitivity().asFile();
        }
        war.addAsLibraries(jars);
    }


    @Test
    public void verifyWhatever() throws Exception {
    }


}
