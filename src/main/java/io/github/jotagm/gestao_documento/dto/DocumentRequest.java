package io.github.jotagm.gestao_documento.dto;

import io.github.jotagm.gestao_documento.model.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Document type is required")
    private DocumentType type;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    @NotNull(message = "File URL is required")
    private String fileUrl;
}
