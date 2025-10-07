<template>
  <div>
    <h1>{{ title }}</h1>

    <div class="form-row">
      <label for="tag-name">Name:</label>
      <input
        type="text"
        id="tag-name"
        v-model="localTagName"
      />
    </div>

    <button @click="handleSave" :disabled="isInvalid">Save</button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

const props = defineProps<{
  title: string
  modelValue?: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'save'): void
}>()

const localTagName = ref(props.modelValue ?? '')

watch(() => props.modelValue, (newVal) => {
    if (newVal && newVal !== localTagName.value){
        localTagName.value = newVal
    }
})

watch(localTagName, (val) => emit('update:modelValue', val))

const isInvalid = computed(() => localTagName.value.trim().length === 0)

function handleSave() {
  if (isInvalid.value) return
  emit('save')
}
</script>
