package com.example.userservice.repositories;

import com.example.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{
    @Override
    Token save(Token token);

    Optional<Token> findByValueAndIsDeleted(String value, boolean isDeleted);
}
