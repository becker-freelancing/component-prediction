package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/apps")
public class BackendGatewayAppsApi {

    private final AppStorageService appStorageService;

    @Autowired
    public BackendGatewayAppsApi(AppStorageService appStorageService) {
        this.appStorageService = appStorageService;
    }

    @PutMapping
    public AppDto save(@RequestBody AppDto appDto) {
        return appStorageService.save(appDto);
    }

    @GetMapping
    public List<AppDto> findAll() {
        return appStorageService.findAll();
    }
}
