<template>
  <li v-if="node.visible">
    <span @click="navigate(node)"
          class="node-label" 
          :class="{active: isActive}"
    >
      {{ node.label }}
    </span>

    <ul v-if="node.children && node.children.length">
      <TreeNode
        v-for="child in node.children"
        :key="child.id"
        :node="child"
      />
    </ul>
  </li>
</template>


<script setup lang="ts">
import {computed} from "vue"
import {useRouter} from 'vue-router'
import TreeNode from './TreeNode.vue'
import type { TreeNodeContent } from './TreeNodeTypes'
import { useActiveNode } from "@/composables/useActiveNode"


const props = defineProps<{
  node: TreeNodeContent
}>()

const router = useRouter()
const {activeNodeId, setActiveNode} = useActiveNode()

const isActive = computed(() => activeNodeId.value === props.node.id)


function navigate(target: TreeNodeContent | undefined) {
  if (target) {
    setActiveNode(target.id)
    router.push(target.seoRoute)
  }
}
</script>

<style scoped>
ul {
  padding-left: 1rem;
}

.node-label {
  cursor: pointer;
  color: #42b883;
}

.node-label:hover {
  text-decoration: underline;
}

.active {
  color:#b3e7c6;
  font-weight: bold;
}
</style>
