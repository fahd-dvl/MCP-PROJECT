package org.example.aiagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

@Component
public class EBankAiAgent {

    private final  ChatClient chatClient;

    public EBankAiAgent(ChatClient.Builder chatClient, ToolCallbackProvider tools){
        this.chatClient=chatClient.defaultTools(tools).defaultSystem("You are an AI banking assistant. " +
                "You MUST use the provided tools to perform actions. " +
                "Do NOT output JSON. Do NOT print the tool parameters. " +
                "Always execute the tool and reply to the user with a natural language sentence confirming the action.").build();
    }

    public String chat(String querry,String conversationId){
        return chatClient.prompt(querry).advisors(a->a.param(ChatMemory.CONVERSATION_ID,conversationId)).call().content();
    }



}
