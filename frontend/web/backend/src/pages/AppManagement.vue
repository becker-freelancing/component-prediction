<template>
  <div>
    <button @click="createApp()">+</button>
  </div>
  <div class="scroll-container">
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
    router.push("/createapp?returnUrl=" + route.fullPath)
}
</script>
