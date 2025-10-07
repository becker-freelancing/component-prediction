<template>
  <div>
    <h1>{{ title }}</h1>

    <div class="form-row">
      <label for="app-name">Name:</label>
      <input
        type="text"
        id="app-name"
        v-model="localAppName"
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

const localAppName = ref(props.modelValue ?? '')

watch(() => props.modelValue, (newVal) => {
    if (newVal && newVal !== localAppName.value){
        localAppName.value = newVal
    }
})

watch(localAppName, (val) => emit('update:modelValue', val))

const isInvalid = computed(() => localAppName.value.trim().length === 0)

function handleSave() {
  if (isInvalid.value) return
  emit('save')
}
</script>
