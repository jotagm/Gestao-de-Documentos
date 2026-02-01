package io.github.jotagm.gestao_documento.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public boolean isExpiringIn30Days() {
        LocalDate threshold = LocalDate.now().plusDays(30);
        return !isExpired() && expirationDate.isBefore(threshold);
    }

    public boolean isExpiringIn7Days() {
        LocalDate threshold = LocalDate.now().plusDays(7);
        return !isExpired() && expirationDate.isBefore(threshold);
    }

    public DocumentStatus calculateStatus() {
        if (isExpired()) {
            return DocumentStatus.EXPIRED;
        } else if (isExpiringIn7Days()) {
            return DocumentStatus.EXPIRING_SOON;
        } else if (isExpiringIn30Days()) {
            return DocumentStatus.EXPIRING;
        }
        return DocumentStatus.VALID;
    }

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        this.status = calculateStatus();
    }
}
