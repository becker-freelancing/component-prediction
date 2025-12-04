<template>
  <AppForm
    title="Edit App"
    v-model="appName"
    @save="save"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { saveApp, fetchAppById } from '@/services/appService'
import type { App } from '@/components/SourceMetadataTypes'
import AppForm from './AppForm.vue'

const appName = ref('')
const id = ref<string | undefined>()
const route = useRoute()
const router = useRouter()

onMounted(async () => {
  const appId = route.query.id as string | undefined
  if (!appId) return
  const app: App | undefined = await fetchAppById(appId)
  if (!app) {
    router.push((route.query.returnUrl as string) ?? '/')
  } else {
    appName.value = app.appName
    id.value = app.id
  }
})

async function save() {
  await saveApp({ id: id.value, appName: appName.value })
  const backQuery = (route.query.returnUrl as string) || '/'
  router.push(backQuery)
}
</script>
