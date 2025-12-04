import type { TreeNodeContent } from "../components/TreeNodeTypes";



export async function fetchNavigation(): Promise<TreeNodeContent[]> {
  return new Promise((resolve, _reject) => {
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
          id: "source-overview",
          label: "Sources",
          seoRoute: "/source-overview",
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
