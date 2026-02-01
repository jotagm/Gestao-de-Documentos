package io.github.jotagm.gestao_documento.model;

import lombok.Getter;

@Getter
public enum DocumentType {
    CNH("Carteira Nacional de Habilitação"),
    CPF("Cadastro de Pessoa Física"),
    RG("Registro Geral"),
    COMPROVANTE_RESIDENCIA("Comprovante de Residência"),
    CERTIDAO_NASCIMENTO("Certidão de Nascimento"),
    CERTIDAO_CASAMENTO("Certidão de Casamento"),
    OUTROS("Outros Documentos");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }
}
