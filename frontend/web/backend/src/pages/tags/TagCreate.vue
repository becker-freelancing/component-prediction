<template>
  <TagForm
    title="Create Tag"
    v-model="tagName"
    @save="save"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { saveTag } from '@/services/tagService'
import TagForm from './TagForm.vue'

const tagName = ref('')
const route = useRoute()
const router = useRouter()

async function save() {
  await saveTag({ id: undefined, tag: tagName.value })
  const backQuery = (route.query.returnUrl as string) || '/'
  router.push(backQuery)
}
</script>
