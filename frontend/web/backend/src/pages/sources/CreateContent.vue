<template>
    <div class="default-margin">
        <h1>Edit or Create Content</h1>

        <div class="form-row">
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

        <div class="form-row">
            <label for="action-title">Action Title</label>
            <input id="action-title" type="text" v-model="title" :class="{ invalid: !titleValid }"></input>
        </div>
        <div class="form-row">
            <label for="action-description">Action Description</label>
            <textarea id="action-description" type="text" v-model="description"
                :class="{ invalid: !descriptionValid }"></textarea>
        </div>
        <div class="form-row">
            <label for="equal-description">Short Description equal to Description</label>
            <input id="equal-description" type="checkbox" v-model="equalDescription"></input>
        </div>
        <div class="form-row">
            <label for="action-short-description">Action Short Description</label>
            <textarea id="action-short-description" type="text" :disabled="equalDescription" v-model="shortDescription"
                :class="{ invalid: !shortDescriptionValid }"></textarea>
        </div>
        <div class="form-row">
            <label for="action-path">In-App Action Path</label>
            <input id="action-path" type="text" v-model="actionPath" :class="{ invalid: !actionPathValid }"></input>
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
        <div class="form-row">
            <label for="locale">Locale</label>
            <input id="locale" type="text" disabled v-model="locale"></input>
        </div>
        <div class="form-row">
            <label for="version">Version</label>
            <input id="version" type="text" disabled v-model="version"></input>
        </div>
        <div class="form-row">
            <label for="created-at">Created At</label>
            <input id="created-at" type="text" disabled v-model="createdAt"></input>
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

const route = useRoute()
const router = useRouter()
const apps = ref<App[]>([])
const originalTags = ref<Tag[]>([])

const id = ref<string>("")
const selectedAppId = ref<string>("")
const title = ref<string>("")
const description = ref<string>("")
const equalDescription = ref<boolean>(false)
const shortDescription = ref<string>("")
const actionPath = ref<string>("")
const locale = ref<string>("--")
const version = ref<number>(1)
const createdAt = ref<DateTime>(DateTime.now())
const selectedTags = ref<Tag[]>([])

const appValid = computed(() => selectedAppId.value !== "")
const titleValid = computed(() => title.value !== "")
const descriptionValid = computed(() => description.value !== "")
const shortDescriptionValid = computed(() => shortDescription.value !== "" || (descriptionValid.value && equalDescription.value))
const actionPathValid = computed(() => actionPath.value !== "")

const tags = computed(() => originalTags.value.filter(t => !selectedTags.value.includes(t)))

onMounted(async () => {
    if (route.query.id) {
        let metaData: SourceMetadata | undefined = await fetchById(route.query.id as string)
        if (metaData) {
            id.value = metaData.id ?? ""
            selectedAppId.value = metaData.app.id ?? ""
            title.value = metaData.actionTitle
            description.value = metaData.actionDescription
            equalDescription.value = metaData.actionDescription === metaData.actionShortDescription
            if (!equalDescription.value) {
                shortDescription.value = metaData.actionShortDescription
            }
            actionPath.value = metaData.inAppActionPath
            locale.value = metaData.locale
            version.value = metaData.version
            createdAt.value = metaData.createdAt ?? DateTime.now()
            selectedTags.value = metaData.tags
        }
    }
    apps.value = await fetchAllApps()
    originalTags.value = await fetchAllTags()
})

function getApp(): App {
    const find: App | undefined = apps.value.find(app => app.id === selectedAppId.value)
    if (!find) {
        throw new Error("App with id " + selectedAppId.value + " not found")
    }
    return find

}

function save() {
    const metaData: SourceMetadata = {
        id: id.value === "" ? undefined : id.value,
        app: getApp(),
        inAppActionPath: actionPath.value,
        actionTitle: title.value,
        actionDescription: description.value,
        actionShortDescription: equalDescription.value ? description.value : shortDescription.value,
        tags: selectedTags.value,
        locale: locale.value,
        version: version.value + 1,
        createdAt: createdAt.value
    }

    saveMetadata(metaData).then(_saved => {
        const returnUrl = route.query.returnUrl as string;
        router.push(returnUrl)
    })
}


</script>