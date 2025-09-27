package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.DocumentDto;

import java.util.List;

public interface StorageService {

    public List<DocumentDto> findAll();
}
