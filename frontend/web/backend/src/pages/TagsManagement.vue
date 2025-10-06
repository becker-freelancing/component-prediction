<template>
  <div class="default-margin">
    <button @click="createTag()">+</button>
  </div>
  <div class="scroll-container">
    <h2 class="default-margin">All Tags</h2>
    <ul v-if="tags.length > 0">
      <li v-for="tag in tags" :key="tag.id">
        {{ tag.tag }}
      </li>
    </ul>
    <p v-else>No Tags found. Create new Tag!</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { Tag } from "@/components/DocumentMetadataTypes";
import { fetchAllTags } from "@/services/tagService";

const tags = ref<Tag[]>([]);
const route = useRoute()
const router = useRouter();

fetchAllTags().then((fetched) => (tags.value = fetched));

function createTag() {
    router.push("/create-tag?returnUrl=" + route.fullPath)
}
</script>
