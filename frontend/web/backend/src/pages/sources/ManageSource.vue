<template>
    <div class="default-margin">
        <h1>{{ isEdit ? "Edit" : "Create" }} Source</h1>

        <div v-if="isEdit" class="form-row">
            <label for="id">ID</label>
            <input id="id" type="text" disabled v-model="id"></input>
        </div>

        <div class="form-row">
            <label for="app-input">App</label>
            <select id="app-input" v-model="selectedAppId" :class="{ invalid: !appValid }">
                <option disabled value="">-- Please select an App --</option>
                <option v-for="app in apps" :key="app.id" :value="app.id">
                    {{ app.appName }}
                </option>
            </select>
        </div>

        <div>
            <component v-if="extractionService" :is="extractionService ? extractionService.page : Loading"/>
        </div>

        <div class="form-row">
            <label for="tags">Tags</label>
            <multiselect v-model="selectedTags" :options="tags" :multiple="true" :close-on-select="false"
                :clear-on-select="false" :preserve-search="true" :taggable="true" tag-placeholder="Add new Tag"
                placeholder="Select Tags..." label="tag" track-by="id">
                <template #tag="{ option, remove }">
                    <span>
                        {{ option.tag }}
                        <button @click="remove(option)">×</button>
                    </span>
                </template>
            </multiselect>
        </div>
        <div v-if="isEdit" class="form-row">
            <label for="locale">Locale</label>
            <input id="locale" type="text" disabled v-model="locale"></input>
        </div>
        <div v-if="isEdit" class="form-row">
            <label for="version">Version</label>
            <input id="version" type="text" disabled v-model="version"></input>
        </div>
        <div v-if="isEdit" class="form-row">
            <label for="created-at">Created At</label>
            <input id="created-at" type="text" disabled v-model="createdAt"></input>
        </div>
        <div v-if="isEdit" class="form-row">
            <label for="last-modified-at">Last Modified At</label>
            <input id="last-modified-at" type="text" disabled v-model="lastModifiedAt"></input>
        </div>
        <button @click="save()">Save</button>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { SourceMetadata, App, Tag } from '@/components/SourceMetadataTypes';
import { fetchById, saveMetadata } from '@/services/sourceMetadataService';
import { fetchAllApps } from '@/services/appService';
import { fetchAllTags } from '@/services/tagService';
import { DateTime } from 'luxon';
import Multiselect from 'vue-multiselect';
import { fetchSourceExtractorByUrlId, type ExtractionService } from "@/services/extractorsService";
import Loading from '@/pages/Loading.vue';

const route = useRoute()
const router = useRouter()
const apps = ref<App[]>([])
const originalTags = ref<Tag[]>([])
const extractionService = ref<ExtractionService | undefined>()
const isEdit = ref<boolean>(false)

const id = ref<string>("")
const selectedAppId = ref<string>("")
const locale = ref<string>("--")
const version = ref<number>(1)
const createdAt = ref<DateTime>(DateTime.now())
const lastModifiedAt = ref<DateTime>(DateTime.now())
const selectedTags = ref<Tag[]>([])


const appValid = computed(() => selectedAppId.value !== "")
const tags = computed(() => originalTags.value.filter(t => !selectedTags.value.includes(t)))

onMounted(async () => {
    if(route.query.id) {
        isEdit.value = true;
    }
    if(route.query.extractorId){
        const extractorId = route.query.extractorId as string;
        extractionService.value = await fetchSourceExtractorByUrlId(extractorId)
    }
    apps.value = await fetchAllApps()
    originalTags.value = await fetchAllTags()
})

function save(){

}
</script>