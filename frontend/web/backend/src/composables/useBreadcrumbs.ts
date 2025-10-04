import { ref, computed, watch } from 'vue'
import type { TreeNodeContent } from '@/components/TreeNodeTypes'
import { useActiveNode } from './useActiveNode'
import { useNavigation } from './useNavigation'

const BASE_URL = import.meta.env.VITE_NAVIGATION_BASE_URL
const NAVIGATION_ID = import.meta.env.VITE_NAVIGATION_ID
const LANGUAGE = import.meta.env.VITE_LANGUAGE


const breadcrumbs = ref<TreeNodeContent[]>([])

export function useBreadcrumbs() {
  const { activeNode } = useActiveNode()
  const { navigationData } = useNavigation()

  async function fetchBreadcrumbs(nodeId: string) {
    try {
      const url = `${BASE_URL}/navigation/${NAVIGATION_ID}/node/${nodeId}/path?language=${LANGUAGE}`
      const response = await fetch(url)
      if (!response.ok) throw new Error('Failed to fetch breadcrumbs')
      const data = await response.json()
      breadcrumbs.value = data._embedded
    } catch (err) {
      console.error('Breadcrumbs fetch error', err)
      breadcrumbs.value = []
    }
  }

  watch(
    [() => activeNode.value, () => navigationData.value],
    ([node, nav]) => {
      if (node && nav && navigationData.value?.length) {
        fetchBreadcrumbs(node.id)
      }
    },
    { immediate: true }
  )

  return { breadcrumbs }
}
