package com.example.rag.controller;

import com.example.rag.dto.AskRequest;
import com.example.rag.dto.AskResponse;
import com.example.rag.service.RagQuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagQuestionController {

    private final RagQuestionService ragQuestionService;

    public RagQuestionController(RagQuestionService ragQuestionService) {
        this.ragQuestionService = ragQuestionService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {
        String answer = ragQuestionService.ask(request.question());
        return new AskResponse(answer);
    }
}