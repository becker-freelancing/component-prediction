<template>
  <nav class="breadcrumbs" v-if="breadcrumbs.length">
    <span v-for="(node, index) in breadcrumbs.slice(1)" :key="node.id">
      <a href="#" @click.prevent="navigate(node)">{{ node.label }}</a>
      <span v-if="index < breadcrumbs.length - 2"> / </span>
    </span>
  </nav>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useActiveNode } from '@/composables/useActiveNode';
import { useBreadcrumbs } from '@/composables/useBreadcrumbs'
import type { TreeNodeContent } from './TreeNodeTypes';


const { breadcrumbs } = useBreadcrumbs()
const { setActiveNode } = useActiveNode()

const router = useRouter()

function navigate(node: TreeNodeContent){
  setActiveNode(node.id)
  router.push(node.seoRoute)
}
</script>


<style scoped>
p {
  color: antiquewhite;
}
.breadcrumbs {
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}
.breadcrumbs a {
  color: #42b883;
  text-decoration: none;
}
.breadcrumbs a:hover {
  text-decoration: underline;
}
</style>
