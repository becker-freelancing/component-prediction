<template>
  <div class="default-margin">
    <button @click="createApp()">+</button>
  </div>
  <div class="scroll-container">
  <h2 class="default-margin">All Apps</h2>
    <ul v-if="apps.length > 0">
      <li v-for="app in apps" :key="app.id">
        {{ app.appName }}
      </li>
    </ul>
    <p v-else>No Apps found. Create new App!</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { App } from "@/components/DocumentMetadataTypes";
import { fetchAllApps } from "@/services/appService";

const apps = ref<App[]>([]);
const route = useRoute()
const router = useRouter();

fetchAllApps().then((fetched) => (apps.value = fetched));

function createApp() {
    router.push("/create-app?returnUrl=" + route.fullPath)
}
</script>
