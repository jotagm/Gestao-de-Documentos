package io.github.jotagm.gestao_documento.repository;

import io.github.jotagm.gestao_documento.model.Document;
import io.github.jotagm.gestao_documento.model.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    List<Document> findByUserId(String userId);

    List<Document> findByStatus(DocumentStatus status);

    @Query("SELECT d FROM Document d WHERE d.expirationDate < :date")
    List<Document> findExpiredDocuments(LocalDate date);

    @Query("SELECT d FROM Document d WHERE d.expirationDate BETWEEN :now AND :threshold")
    List<Document> findExpiringDocuments(LocalDate now, LocalDate threshold);

    @Query("SELECT d FROM Document d WHERE d.user.id = :userId AND d.type = :type")
    List<Document> findByUserIdAndType(String userId, String type);
}
