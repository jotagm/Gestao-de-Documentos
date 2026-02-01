package io.github.jotagm.gestao_documento.controller;

import io.github.jotagm.gestao_documento.dto.DocumentResponse;
import io.github.jotagm.gestao_documento.model.Document;
import io.github.jotagm.gestao_documento.model.DocumentType;
import io.github.jotagm.gestao_documento.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestParam String userId,
            @RequestParam DocumentType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate,
            @RequestParam MultipartFile file) {

        Document document = documentService.registerDocument(
            userId,
            type,
            issueDate,
            expirationDate,
            file
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DocumentResponse.fromEntity(document));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentResponse>> getUserDocuments(
            @PathVariable String userId) {

        List<Document> documents = documentService.getUserDocuments(userId);
        List<DocumentResponse> response = documents.stream()
                .map(DocumentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable String documentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newExpirationDate,
            @RequestParam(required = false) MultipartFile newFile) {

        Document updated = documentService.updateDocument(
            documentId,
            newExpirationDate,
            newFile
        );

        return ResponseEntity.ok(DocumentResponse.fromEntity(updated));
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<DocumentResponse>> getExpiringDocuments() {
        List<Document> documents = documentService.getExpiringDocuments();
        List<DocumentResponse> response = documents.stream()
                .map(DocumentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<DocumentResponse>> getExpiredDocuments() {
        List<Document> documents = documentService.getExpiredDocuments();
        List<DocumentResponse> response = documents.stream()
                .map(DocumentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
