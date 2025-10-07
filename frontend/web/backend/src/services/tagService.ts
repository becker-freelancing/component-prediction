import type { Tag } from "@/components/DocumentMetadataTypes";
import { fetchGet, fetchPut } from "./fetchService";

export async function fetchAllTags(): Promise<Tag[]> {
  return fetchGet<Tag[]>("/backend/api/tags").then((data) =>
    data ? data : []
  );
}

export async function saveTag(app: Tag): Promise<Tag> {
  return fetchPut<Tag>("/backend/api/tags", app).then((data) => {
    if (data) {
      return data;
    }

    throw Error("Failed to save Tag");
  });
}


export async function fetchTagById(id: string): Promise<Tag | undefined> {
  return fetchAllTags()
  .then(tags => tags.find(tag => tag.id === id))
}
