import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import type { TreeNodeContent } from '@/components/TreeNodeTypes'
import { useNavigation } from './useNavigation'

const activeNodeId = ref<string | null>(null)

export function useActiveNode() {
    const route = useRoute()
  const { navigationData } = useNavigation()

  function setActiveNode(id: string) {
    activeNodeId.value = id
  }

  function setActiveNodeByRoute(seoRoute: string) {
    if (!navigationData.value) return
    const node = findNodeByRoute(navigationData.value, seoRoute)
    if (node) activeNodeId.value = node.id
  }

  function findNodeByRoute(nodes: TreeNodeContent[], routePath: string): TreeNodeContent | null {
    for (const node of nodes) {
      if (node.seoRoute === routePath) return node
      if (node.children) {
        const found = findNodeByRoute(node.children, routePath)
        if (found) return found
      }
    }
    return null
  }

  const activeNode = computed(() => {
    if (!navigationData.value || !activeNodeId.value) return null
    function findById(nodes: TreeNodeContent[], id: string): TreeNodeContent | null {
      for (const node of nodes) {
        if (node.id === id) return node
        if (node.children) {
          const found = findById(node.children, id)
          if (found) return found
        }
      }
      return null
    }
    return findById(navigationData.value, activeNodeId.value)
  })

  watch(
    () => navigationData.value,
    (val) => {
      if (val && val.length > 0) {
        setActiveNodeByRoute(route.fullPath)
      }
    },
    { immediate: true }
  )

  return { activeNodeId, activeNode, setActiveNode, setActiveNodeByRoute }
}
