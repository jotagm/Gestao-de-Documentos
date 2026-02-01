package io.github.jotagm.gestao_documento.service;

import io.github.jotagm.gestao_documento.model.Document;
import io.github.jotagm.gestao_documento.model.DocumentType;
import io.github.jotagm.gestao_documento.model.User;
import io.github.jotagm.gestao_documento.repository.DocumentRepository;
import io.github.jotagm.gestao_documento.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Document registerDocument(String userId, DocumentType type, LocalDate issueDate,
                                      LocalDate expirationDate, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String fileUrl = fileStorageService.storeFile(file);

        Document document = Document.builder()
                .user(user)
                .type(type)
                .issueDate(issueDate)
                .expirationDate(expirationDate)
                .fileUrl(fileUrl)
                .build();

        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<Document> getUserDocuments(String userId) {
        return documentRepository.findByUserId(userId);
    }

    @Transactional
    public Document updateDocument(String documentId, LocalDate newExpirationDate, MultipartFile newFile) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        document.setExpirationDate(newExpirationDate);

        if (newFile != null && !newFile.isEmpty()) {
            fileStorageService.deleteFile(document.getFileUrl());
            String newFileUrl = fileStorageService.storeFile(newFile);
            document.setFileUrl(newFileUrl);
        }

        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<Document> getExpiringDocuments() {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(30);
        return documentRepository.findExpiringDocuments(now, threshold);
    }

    @Transactional(readOnly = true)
    public List<Document> getExpiredDocuments() {
        return documentRepository.findExpiredDocuments(LocalDate.now());
    }

    @Transactional
    public void deleteDocument(String documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        fileStorageService.deleteFile(document.getFileUrl());
        documentRepository.delete(document);
    }
}
