<template>
  <AppForm
    title="Create App"
    v-model="appName"
    @save="save"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { saveApp } from '@/services/appService'
import AppForm from './AppForm.vue'

const appName = ref('')
const route = useRoute()
const router = useRouter()

async function save() {
  await saveApp({ id: undefined, appName: appName.value })
  const backQuery = (route.query.returnUrl as string) || '/'
  router.push(backQuery)
}
</script>
