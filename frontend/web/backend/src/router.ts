import { createRouter, createWebHistory } from 'vue-router'
import NotFound from './pages/NotFound.vue'
import CreateContent from './pages/CreateContent.vue'
import AppManagement from './pages/AppManagement.vue'
import AppCreate from './pages/AppCreate.vue'

const routes = [
  {
    path: "/manage-apps",
    name: "manageApps",
    component: AppManagement
  },
  {
    path: "/createapp",
    name: "createApp",
    component: AppCreate
  },
  {
    path: '/create-content',
    name: 'createContent',
    component: CreateContent,
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
