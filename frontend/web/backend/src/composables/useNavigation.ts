import { ref, onMounted } from "vue";
import { fetchNavigation } from "@/services/navigationService";
import type { TreeNodeContent } from "@/components/TreeNodeTypes";

export function useNavigation() {
  const navigationData = ref<TreeNodeContent[]>([]);
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  const loadNavigation = async () => {
    isLoading.value = true;
    error.value = null;
    try {
      navigationData.value = await fetchNavigation();
    } catch (err: any) {
      error.value = err.message || "Unknown error";
      console.error(err);
    } finally {
      isLoading.value = false;
    }
  };

  onMounted(() => {
    loadNavigation();
  });

  return { navigationData, isLoading, error, loadNavigation };
}
