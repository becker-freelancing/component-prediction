<template>
  <h2 class="default-margin">All Apps</h2>
  <div class="default-margin">
    <button @click="createApp()">Create</button>
    <button @click="editApp()" :disabled="selectedAppId === ''">Edit</button>
  </div>
  <div class="scroll-container">
  <SelectableTable
    :items="apps"
    :columns="['appName']"
    :columnsMapping="{'appName': 'Name'}"
    :idAccessor="appIdAccessor"
    v-model="selectedAppId"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { App } from "@/components/SourceMetadataTypes";
import { fetchAllApps } from "@/services/appService";
import SelectableTable from "@/components/SelectableTable.vue";


const apps = ref<App[]>([]);
const route = useRoute()
const router = useRouter();
const selectedAppId = ref<string>("")

fetchAllApps().then((fetched) => (apps.value = fetched));

function appIdAccessor(app: App): string {
  return app.id ?? "";
}

function createApp() {
    router.push("/create-app?returnUrl=" + route.fullPath)
}

function editApp(){
  if(selectedAppId.value !== ""){
    router.push("/edit-app?returnUrl=" + route.fullPath + "&id=" + selectedAppId.value)
  }
}
</script>
