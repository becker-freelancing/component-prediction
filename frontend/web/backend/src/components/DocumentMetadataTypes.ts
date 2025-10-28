import {DateTime} from 'luxon';

export interface App {
    id: string | undefined,
    appName: string
}

export interface Tag {
    id: string | undefined,
    tag: string
}

export interface DocumentMetadata {
    id: string | undefined,
    app: App,
    inAppActionPath: string,
    actionTitle: string,
    actionDescription: string,
    actionShortDescription: string,
    tags: Tag[]
    locale: string,
    version: number,
    createdAt: DateTime | undefined,
}