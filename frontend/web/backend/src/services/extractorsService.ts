import type { Component } from "vue";
import { fetchGet } from "./fetchService";
import ManageManualSource from "@/pages/sources/ManageManualSource.vue";

export interface ExtractionService{
    className: string,
    clearName: string,
    urlId: string,
    page: Component | undefined
}

const EXTRACTION_SERVICES: ExtractionService[] = [
    {
        className: "com.becker.freelance.component.prediction.extraction.api.ApiManualInputExtractionService",
        clearName: "Manual Input",
        urlId: crypto.randomUUID(),
        page: ManageManualSource
    }
];

let fetchedExtractionServices: ExtractionService[] | undefined = undefined;

export async function fetchAllSourceExtractorIds(): Promise<ExtractionService[]> {
    if(fetchedExtractionServices){
        return new Promise((resolve, _reject) => resolve(fetchedExtractionServices ? fetchedExtractionServices : []))
    }
    return fetchGet<string[]>("/backend/api/sourceextractors")
    .then((data) => data ? data : [])
    .then((data) => EXTRACTION_SERVICES.filter(service => data.includes(service.className)))
    .then((services) => {
        fetchedExtractionServices = services;
        return services;
    });
}

export async function fetchSourceExtractorByUrlId(urlId: string): Promise<ExtractionService | undefined> {
    const services = await fetchAllSourceExtractorIds()
    return new Promise((resolve, _reject) => {
        resolve(services.find((extractor => extractor.urlId === urlId )))
    })
}