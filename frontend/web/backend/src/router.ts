import { createRouter, createWebHistory } from 'vue-router'
import NotFound from './pages/NotFound.vue'
import CreateContent from './pages/content/CreateContent.vue'
import AppCreate from './pages/app/AppCreate.vue'
import TagsOverview from './pages/tags/TagsOverview.vue'
import TagCreate from './pages/tags/TagCreate.vue'
import TagEdit from './pages/tags/TagEdit.vue'
import ContentManagement from './pages/content/ContentManagement.vue'
import AppOverview from './pages/app/AppOverview.vue'
import AppEdit from './pages/app/AppEdit.vue'

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
    path: '/manage-content',
    name: 'manageContent',
    component: ContentManagement,
  },
  {
    path: "/create-content",
    name: "createContent",
    component: CreateContent
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
