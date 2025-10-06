import type { App } from "@/components/DocumentMetadataTypes";
import { fetchGet, fetchPut } from "./fetchService";

export async function fetchAllApps(): Promise<App[]> {
  return fetchGet<App[]>("/backend/api/apps").then((data) =>
    data ? data : []
  );
}

export async function saveApp(app: App): Promise<App> {
  return fetchPut<App>("/backend/api/apps", app).then((data) => {
    if (data) {
      return data;
    }

    throw Error("Failed to save App");
  });
}
