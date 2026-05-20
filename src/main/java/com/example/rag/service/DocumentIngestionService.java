package com.example.rag.service;

import com.example.rag.model.SourceDocument;
import com.example.rag.repository.SourceDocumentRepository;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private final SourceDocumentRepository repository;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    @Transactional
    public void ingestPdf(MultipartFile file) throws IOException {
        // 1. Save to SQL (SourceDocument Entity)
        System.out.println("inside ingestion service, begin save document");
        SourceDocument doc = new SourceDocument();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setUploadDate(LocalDateTime.now());
        SourceDocument savedDoc = repository.save(doc);
        System.out.println("inside ingestion service, end save document");
        // 2. Load PDF into LangChain4j Document object
        Document document = new ApachePdfBoxDocumentParser().parse(file.getInputStream());

        document.metadata().put("source_document_id", savedDoc.getId());

        System.out.println("inside ingestion service, begin ingest document");
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 50))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);
        System.out.println("inside ingestion service, end ingest document");
    }
}
