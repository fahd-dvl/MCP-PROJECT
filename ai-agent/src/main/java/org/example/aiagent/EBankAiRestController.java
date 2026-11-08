package org.example.aiagent;

import org.example.aiagent.agents.EBankAiAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
public class EBankAiRestController {

    private EBankAiAgent eBankAiAgent;

    public EBankAiRestController(EBankAiAgent eBankAiAgent){
        this.eBankAiAgent=eBankAiAgent;
    }
    @PostMapping(value = "/api/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestBody String querry){
        return eBankAiAgent.chat(querry);
    }
}
