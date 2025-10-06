<template>
    <h1>Create Tag</h1>

    <div class="form-row">
        <label for="tag-name">Name:</label>
        <input type="text" id="tag-name" v-model="tagName"/>
    </div>

    <button @click="save()" :disabled="isValid">Save</button>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { computed, ref } from 'vue';
import { saveTag } from '@/services/tagService';

const route = useRoute()
const router = useRouter()

const tagName = ref('')

function validate(): boolean{
    return tagName.value.length > 0;
}

const isValid = computed(() => !validate())

function save(){
    if(!validate()){
        return;
    }
    
    saveTag({id: undefined, tag: tagName.value})
    .then(saved => {
        const backQuery = (route.query.returnUrl as string)|| '/'
        router.push(backQuery)
    })
}
</script>

<style scoped>

</style>