import type { TreeNodeContent } from "../components/TreeNodeTypes";



export async function fetchNavigation(): Promise<TreeNodeContent[]> {
  return new Promise((resolve, reject) => {
    resolve(
      [
        {
          id: "management",
          label: "Management",
          seoRoute: "/manage-apps",
          visible: true,
          permissions: [],
          children: [
        {
          id: "manage-apps",
          label: "Apps",
          seoRoute: "/manage-apps",
          visible: true,
          permissions: [],
          children: []
        },
        {
          id: "manage-tags",
          label: "Tags",
          seoRoute: "/manage-tags",
          visible: true,
          permissions: [],
          children: []
        },
        {
          id: "create-content",
          label: "Content",
          seoRoute: "/manage-content",
          visible: true,
          permissions: [],
          children: []
        }
      ]
        }
      ]
      
    )
  }
  )
}
