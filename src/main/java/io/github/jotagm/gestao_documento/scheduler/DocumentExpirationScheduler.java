package io.github.jotagm.gestao_documento.scheduler;

import io.github.jotagm.gestao_documento.model.Document;
import io.github.jotagm.gestao_documento.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentExpirationScheduler {

    private final DocumentRepository documentRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void checkExpiringDocuments() {
        log.info("Starting daily document expiration check...");

        try {
            LocalDate now = LocalDate.now();
            LocalDate threshold = now.plusDays(30);
            List<Document> expiringDocuments = documentRepository.findExpiringDocuments(now, threshold);

            log.info("Found {} documents expiring in the next 30 days", expiringDocuments.size());

            expiringDocuments.forEach(doc -> {
                log.warn("Document {} of type {} for user {} will expire on {}",
                        doc.getId(),
                        doc.getType(),
                        doc.getUser().getName(),
                        doc.getExpirationDate());
            });

            log.info("Daily document expiration check completed successfully.");
        } catch (Exception e) {
            log.error("Error checking expiring documents: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void checkExpiredDocuments() {
        log.info("Starting evening expired document check...");

        try {
            List<Document> expiredDocuments = documentRepository.findExpiredDocuments(LocalDate.now());

            log.info("Found {} expired documents", expiredDocuments.size());

            expiredDocuments.forEach(doc -> {
                log.error("EXPIRED: Document {} of type {} for user {} expired on {}",
                        doc.getId(),
                        doc.getType(),
                        doc.getUser().getName(),
                        doc.getExpirationDate());
            });

            log.info("Evening expired document check completed successfully.");
        } catch (Exception e) {
            log.error("Error checking expired documents: {}", e.getMessage(), e);
        }
    }
}
