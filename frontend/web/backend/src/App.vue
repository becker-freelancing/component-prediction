<template>
  <div class="app-container">
    <nav class="sidebar">
      <h1>Navigation Tree</h1>

      <div v-if="isLoading">Loading Navigation...</div>
      <div v-else-if="error">{{ error }}</div>
      <ul v-else>
        <TreeNode
          v-for="child in navigationData"
          :key="child.id"
          :node="child"
        />
      </ul>
    </nav>

    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import TreeNode from "@/components/TreeNode.vue";
import { useNavigation } from "@/composables/useNavigation";

const { navigationData, isLoading, error } = useNavigation();
</script>

<style>
.app-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  font-family: Arial, sans-serif;
}

.sidebar {
  box-sizing: border-box;
  padding: 1rem;
  border-right: 1px solid #ccc;
  overflow-y: auto;
  flex: 0 0 20%;
  height: 100%;
}

.content {
  flex: 1;
  box-sizing: border-box;
  padding: 1rem;
  overflow-y: auto;
}
</style>
