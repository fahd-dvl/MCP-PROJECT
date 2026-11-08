package org.example.aiagent.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.aiagent.agents.EBankAiAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DiscordController extends ListenerAdapter {

    private final EBankAiAgent ebankAgent;

    @Value("${discord.token}")
    private String token;

    public DiscordController(EBankAiAgent ebankAgent) {
        this.ebankAgent = ebankAgent;
    }

    @PostConstruct
    void start() throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build();
        jda.awaitReady();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String question = event.getMessage().getContentRaw();
        String answer = ebankAgent.chat(question); // adjust to your agent's real method
        event.getChannel().sendMessage(answer).queue();
    }
}