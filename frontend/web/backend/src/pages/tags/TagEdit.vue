<template>
  <TagForm
    title="Edit Tag"
    v-model="tagName"
    @save="save"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { saveTag, fetchTagById } from '@/services/tagService'
import type { Tag } from '@/components/DocumentMetadataTypes'
import TagForm from './TagForm.vue'

const tagName = ref('')
const id = ref<string | undefined>()
const route = useRoute()
const router = useRouter()

onMounted(async () => {
  const tagId = route.query.id as string | undefined
  if (!tagId) return
  const tag: Tag | undefined = await fetchTagById(tagId)
  if (!tag) {
    router.push((route.query.returnUrl as string) ?? '/')
  } else {
    tagName.value = tag.tag
    id.value = tag.id
  }
})

async function save() {
  await saveTag({ id: id.value, tag: tagName.value })
  const backQuery = (route.query.returnUrl as string) || '/'
  router.push(backQuery)
}
</script>
