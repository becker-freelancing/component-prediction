import type { DocumentMetadata } from "@/components/DocumentMetadataTypes";
import { fetchGet, fetchPut } from "./fetchService";

export async function fetchById(
  id: string
): Promise<DocumentMetadata | undefined> {
  return fetchGet<DocumentMetadata>("/backend/api/documents/metadata?id=" + id);
}

export async function fetchAllMetadata(): Promise<DocumentMetadata[]> {
  return fetchGet<DocumentMetadata[]>(
    "/backend/api/documents/metadata/all"
  ).then((data) => (data ? data : []));
}

export async function saveMetadata(
  metadata: DocumentMetadata
): Promise<DocumentMetadata> {
  return fetchPut<DocumentMetadata>(
    "/backend/api/documents/metadata",
    metadata
  ).then((data) => {
    if (data) {
      return data;
    }
    throw new Error("Could not save document " + metadata);
  });
}
