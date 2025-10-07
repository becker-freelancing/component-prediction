<template>
  <h2 class="default-margin">All Tags</h2>
  <div class="default-margin">
    <button @click="createTag()">Create</button>
    <button @click="editTag()" :disabled="selectedTagId === ''">Edit</button>
  </div>
  <div class="scroll-container">
  <SelectableTable
    :items="tags"
    :columns="['tag']"
    :columnsMapping="{'tag': 'Tag Name'}"
    :idAccessor="tagIdAccessor"
    v-model="selectedTagId"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import type { Tag } from "@/components/DocumentMetadataTypes";
import { fetchAllTags } from "@/services/tagService";
import SelectableTable from "@/components/SelectableTable.vue";

const tags = ref<Tag[]>([]);
const route = useRoute()
const router = useRouter();
const selectedTagId = ref<string>("")

function tagIdAccessor(tag: Tag): string {
  return tag.id ?? "";
}

fetchAllTags().then((fetched) => (tags.value = fetched));

function createTag() {
    router.push("/create-tag?returnUrl=" + route.fullPath)
}

function editTag(){
  if(selectedTagId.value !== ""){
    router.push("/edit-tag?returnUrl=" + route.fullPath + "&id=" + selectedTagId.value)
  }
}
</script>
