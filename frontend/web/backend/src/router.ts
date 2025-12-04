import { createRouter, createWebHistory } from 'vue-router'
import NotFound from './pages/NotFound.vue'
import CreateContent from './pages/sources/CreateContent.vue'
import AppCreate from './pages/app/AppCreate.vue'
import TagsOverview from './pages/tags/TagsOverview.vue'
import TagCreate from './pages/tags/TagCreate.vue'
import TagEdit from './pages/tags/TagEdit.vue'
import SourceOverview from './pages/sources/SourceOverview.vue'
import AppOverview from './pages/app/AppOverview.vue'
import AppEdit from './pages/app/AppEdit.vue'
import ManageSource from './pages/sources/ManageSource.vue'

const routes = [
  {
    path: "/manage-apps",
    name: "manageApps",
    component: AppOverview
  },
  {
    path: "/create-app",
    name: "createApp",
    component: AppCreate
  },
  {
    path: "/edit-app",
    name: "editApp",
    component: AppEdit
  },
  {
    path: "/manage-tags",
    name: "manageTags",
    component: TagsOverview
  },
  {
    path: "/create-tag",
    name: "createTag",
    component: TagCreate
  },
  {
    path: "/edit-tag",
    name: "editTag",
    component: TagEdit
  },
  {
    path: '/source-overview',
    name: 'manageContent',
    component: SourceOverview,
  },
  {
    path: "/manage-source",
    name: "manage-source",
    component: ManageSource
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'content',
    component: NotFound,
  },

]

export default createRouter({
  history: createWebHistory(),
  routes,
})
