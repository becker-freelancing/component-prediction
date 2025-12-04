import {DateTime} from 'luxon';

export interface App {
    id: string | undefined,
    appName: string
}

export interface Tag {
    id: string | undefined,
    tag: string
}

export interface SourceMetadata {
    id: string | undefined,
    app: App,
    tags: Tag[],
    locale: string,
    version: number,
    createdAt: DateTime | undefined,
    lastModifiedAt: DateTime | undefined,
    fileName: string | undefined,
    parent: SourceMetadata | undefined,
    hasChildren: boolean
}