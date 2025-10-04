export interface TreeNodeContent {
    id: string,
    label: string,
    seoRoute: string,
    visible: boolean,
    permissions: string[] | undefined,
    children: TreeNodeContent[]
}

