<template>
  <div class="default-margin">
    <button @click="createDocument()">+</button>
  </div>
  <div class="scroll-container">
  <h2 class="default-margin">All Documents</h2>
    <ul v-if="metaData.length > 0">
      <li v-for="data in metaData" :key="data.id">
        {{ data.actionTitle }}
      </li>
    </ul>
    <p v-else>No Documents found. Create new Documents!</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { DocumentMetadata } from "@/components/DocumentMetadataTypes";
import { fetchAllMetadata } from "@/services/documentMetadataService";

const metaData = ref<DocumentMetadata[]>([]);
const route = useRoute()
const router = useRouter();

fetchAllMetadata().then((fetched) => (metaData.value = fetched));

function createDocument() {
    router.push("/create-content?returnUrl=" + route.fullPath)
}
</script>
