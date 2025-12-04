import type { SourceMetadata } from "@/components/SourceMetadataTypes";
import { fetchGet, fetchPut } from "./fetchService";

export async function fetchById(
  id: string
): Promise<SourceMetadata | undefined> {
  return fetchGet<SourceMetadata>("/backend/api/sources/metadata?id=" + id);
}

export async function fetchAllMetadata(): Promise<SourceMetadata[]> {
  return fetchGet<SourceMetadata[]>(
    "/backend/api/sources/metadata/all"
  ).then((data) => (data ? data : []));
}

export async function saveMetadata(
  metadata: SourceMetadata
): Promise<SourceMetadata> {
  return fetchPut<SourceMetadata>(
    "/backend/api/sources/metadata",
    metadata
  ).then((data) => {
    if (data) {
      return data;
    }
    throw new Error("Could not save document " + metadata);
  });
}
