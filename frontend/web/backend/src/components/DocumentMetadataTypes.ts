import {DateTime} from 'luxon';

export interface App {
    id: number | undefined,
    appName: string
}

export interface Tag {
    id: number | undefined,
    appName: string
}

export interface DocumentMetadata {
    id: number | undefined,
    documentId: string | undefined,
    app: App,
    inAppActionPath: string,
    actionTitle: string,
    actionDescription: string,
    actionShortDescription: string,
    locale: string,
    version: number,
    createdAt: DateTime | undefined,

}