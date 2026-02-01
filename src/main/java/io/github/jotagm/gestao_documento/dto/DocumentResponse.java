package io.github.jotagm.gestao_documento.dto;

import io.github.jotagm.gestao_documento.model.Document;
import io.github.jotagm.gestao_documento.model.DocumentStatus;
import io.github.jotagm.gestao_documento.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private String id;
    private String userId;
    private String userName;
    private DocumentType type;
    private String typeDescription;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private DocumentStatus status;
    private String fileUrl;
    private Long daysUntilExpiration;

    public static DocumentResponse fromEntity(Document document) {
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), document.getExpirationDate());

        return DocumentResponse.builder()
                .id(document.getId())
                .userId(document.getUser().getId())
                .userName(document.getUser().getName())
                .type(document.getType())
                .typeDescription(document.getType().getDescription())
                .issueDate(document.getIssueDate())
                .expirationDate(document.getExpirationDate())
                .status(document.getStatus())
                .fileUrl(document.getFileUrl())
                .daysUntilExpiration(daysUntil)
                .build();
    }
}
