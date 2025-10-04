package com.becker.freelance.component.prediction.gateway.api;


import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/backend/api/tags")
public class BackendGatewayTagsApi {


    private final TagsStorageService tagsStorageService;

    @Autowired
    public BackendGatewayTagsApi(TagsStorageService tagsStorageService) {
        this.tagsStorageService = tagsStorageService;
    }

    @PutMapping
    public TagDto save(@RequestBody TagDto appDto) {
        return tagsStorageService.save(appDto);
    }

    @GetMapping
    public List<TagDto> findAll() {
        return tagsStorageService.findAll();
    }
}
