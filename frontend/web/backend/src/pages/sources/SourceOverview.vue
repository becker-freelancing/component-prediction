<template>
  <div class="default-margin dropdown">
    <button @click="toggleDropdown()" class="dropdown-button">Add Source</button>

    <ul v-if="open" class="dropdown-menu">
      <li v-for="extractor in extractors" @click="createSource(extractor)"><button>{{ extractor.clearName }}</button></li>
    </ul>
  </div>
  <div class="scroll-container">
  <h2 class="default-margin">All Sources</h2>
    <ul v-if="metaData.length > 0">
      <li v-for="data in metaData" :key="data.id">
      </li>
    </ul>
    <p v-else>No Sources found. Create new Source!</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { SourceMetadata } from "@/components/SourceMetadataTypes";
import { fetchAllMetadata } from "@/services/sourceMetadataService";
import { fetchAllSourceExtractorIds } from "@/services/extractorsService";
import type { ExtractionService } from "@/services/extractorsService";

const metaData = ref<SourceMetadata[]>([]);
const extractors = ref<ExtractionService[]>([]);
const route = useRoute()
const router = useRouter();

const open = ref(false)

const toggleDropdown = () => {
  open.value = !open.value
}

fetchAllMetadata().then((fetched) => (metaData.value = fetched));
fetchAllSourceExtractorIds().then((fetched) => (extractors.value = fetched))

function createSource(extractor: ExtractionService) {
    router.push("/manage-source?returnUrl=" + route.fullPath + "&extractorId=" + extractor.urlId)
}
</script>


<style scoped>
.dropdown {
  position: relative;
  display: inline-block;
}
.dropdown-button {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  border-radius: 6px;
  list-style: none;
  margin: 4px 0 0;
  padding: 0;
  width: 150px;
}
.dropdown-menu li {
  padding: 8px 12px;
  cursor: pointer;
}

</style>