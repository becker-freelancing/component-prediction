import type { TreeNodeContent } from "../components/TreeNodeTypes";



export async function fetchNavigation(): Promise<TreeNodeContent[]> {
  return new Promise((resolve, reject) => {
    resolve(
      [
        {
          id: "manage-apps",
          label: "Manage Apps",
          seoRoute: "/manage-apps",
          visible: true,
          permissions: [],
          children: []
        },
        {
          id: "manage-tags",
          label: "Manage Tags",
          seoRoute: "/manage-tags",
          visible: true,
          permissions: [],
          children: []
        },
        {
          id: "create-content",
          label: "Create Content",
          seoRoute: "/create-content",
          visible: true,
          permissions: [],
          children: []
        }
      ]
    )
  }
  )
}
