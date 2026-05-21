package com.example.rag.controller;

import com.example.rag.service.DocumentIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/documents")
public class DocumentIngestionController {

    private final DocumentIngestionService ingestionService;

    public DocumentIngestionController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            ingestionService.ingestPdf(file);
            return ResponseEntity.ok("Document processed and vectorized successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process PDF: " + e.getMessage());
        }
    }
}
