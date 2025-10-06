import { createRouter, createWebHistory } from 'vue-router'
import NotFound from './pages/NotFound.vue'
import CreateContent from './pages/CreateContent.vue'
import AppManagement from './pages/AppManagement.vue'
import AppCreate from './pages/AppCreate.vue'
import TagsManagement from './pages/TagsManagement.vue'
import TagCreate from './pages/TagCreate.vue'
import ContentManagement from './pages/ContentManagement.vue'

const routes = [
  {
    path: "/manage-apps",
    name: "manageApps",
    component: AppManagement
  },
  {
    path: "/create-app",
    name: "createApp",
    component: AppCreate
  },
  {
    path: "/manage-tags",
    name: "manageTags",
    component: TagsManagement
  },
  {
    path: "/create-tag",
    name: "createTag",
    component: TagCreate
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
