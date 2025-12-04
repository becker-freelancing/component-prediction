package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorFactory;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/api/sourceextractors")
public class BackendGatewaySourceExtractors {

    private final List<String> extractorIds;

    @Autowired
    public BackendGatewaySourceExtractors(SourceContentExtractorFactory sourceContentExtractorFactory){
        extractorIds = sourceContentExtractorFactory.findAllIdProvider().stream()
                .map(SourceContentExtractorIdProvider::getOriginalClassName)
                .toList();
    }

    @GetMapping
    public List<String> findAll(){
        return extractorIds;
    }

}
