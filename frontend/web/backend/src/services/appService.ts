import type { App } from "@/components/SourceMetadataTypes";
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

export async function fetchAppById(id: string): Promise<App | undefined> {
  return fetchAllApps()
  .then(apps => apps.find(app => app.id === id))
}
