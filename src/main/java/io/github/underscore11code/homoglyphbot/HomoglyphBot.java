package io.github.underscore11code.homoglyphbot;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import ninja.egg82.homoglyph.HomoglyphHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.EnumSet;

public class HomoglyphBot {
    @Getter private static HomoglyphBot instance;
    @Getter private JDA jda;
    @Getter private HomoglyphHelper homoglyphHelper;
    private static final Logger logger = LoggerFactory.getLogger("Main");

    /**
     * Bootstrap to init.
     * @param args startup args
     */
    public static void main(String[] args) {
        String token = null;
        // First try env variables
        if (System.getenv("HOMOGLYPH_BOT_TOKEN") != null) {
            token = System.getenv("HOMOGLYPH_BOT_TOKEN");
        }
        // Override with startup args, if present
        if (args.length == 1) {
            token = args[0];
        }

        if (token == null) {
            logger.error("No token found! Please either define a \"HOMOGLYPH_BOT_TOKEN\" environment variable with the token, " +
                    "or pass the token as a startup argument");
            System.exit(1);
        }

        instance = new HomoglyphBot(token);
    }

    private HomoglyphBot(String token) {
        logger.info("Starting up!");
        try {
            jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(new EventListener())
                    .build();
        } catch (LoginException e) {
            logger.error("Error logging in: " + e.getMessage(), e);
            System.exit(1);
        }
        try {
            homoglyphHelper = HomoglyphHelper.create();
        } catch (IOException e) {
            logger.error("Error creating HomoglyphHelper: " + e.getMessage(), e);
        }
    }

    /**
     * Tidies up a name for Discord
     * @param name the name to tidy up
     * @return cleaned name
     */
    public String handleName(String name) {
        return homoglyphHelper.toAlphanumeric(name);
    }
}
