package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;

import java.util.List;

public interface AppStorageService {

    public List<AppDto> findAll();

    public AppDto save(AppDto appDto);
}
