package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;

import java.util.List;

public interface TagsStorageService {

    public TagDto save(TagDto dto);

    public List<TagDto> findAll();
}
