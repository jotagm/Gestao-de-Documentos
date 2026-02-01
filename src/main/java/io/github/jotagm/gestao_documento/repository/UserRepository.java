package io.github.jotagm.gestao_documento.repository;

import io.github.jotagm.gestao_documento.model.User;
import io.github.jotagm.gestao_documento.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByStatus(UserStatus status);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.documents WHERE u.id = :id")
    Optional<User> findByIdWithDocuments(String id);

    @Query("SELECT u FROM User u WHERE u.status = 'BLOCKED'")
    List<User> findBlockedUsers();

    boolean existsByEmail(String email);
}
