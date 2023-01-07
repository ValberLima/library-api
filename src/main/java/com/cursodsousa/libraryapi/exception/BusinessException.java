package com.cursodsousa.libraryapi.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String isbnJáCadastrado){
        super(isbnJáCadastrado);
    }
}
