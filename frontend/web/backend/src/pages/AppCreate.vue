<template>
    <h1>Create App</h1>

    <div class="form-row">
        <label for="app-name">Name:</label>
        <input type="text" id="app-name" v-model="appName"/>
    </div>

    <button @click="save()" :disabled="isValid">Save</button>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { computed, ref } from 'vue';
import { saveApp } from '@/services/appService';

const route = useRoute()
const router = useRouter()

const appName = ref('')

function validate(): boolean{
    return appName.value.length > 0;
}

const isValid = computed(() => !validate())

function save(){
    if(!validate()){
        return;
    }
    
    saveApp({id: undefined, appName: appName.value})
    .then(saved => {
        const backQuery = (route.query.returnUrl as string)|| '/'
        router.push(backQuery)
    })
}
</script>

<style scoped>

</style>