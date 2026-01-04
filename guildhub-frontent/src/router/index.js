import { createRouter, createWebHistory } from "vue-router";
import RegisterPage from "@/pages/authentication/RegisterPage.vue";
import LoginPage from "@/pages/authentication/LoginPage.vue";
import HomePage from "@/pages/HomePage.vue";
import TeamsPage from "@/pages/TeamsPage.vue";
import AdminPanel from "@/pages/AdminPanel.vue";
import NewsListPage from "@/pages/NewsListPage.vue";
import NewsDetailPage from "@/pages/NewsDetailPage.vue";

const routes = [
    {path: '/register', component: RegisterPage},
    {path: '/login', component: LoginPage},
    {path: '/teams', component: TeamsPage},
    {path: '/admin', component: AdminPanel},
    {path: '/news', component: NewsListPage},
    {path: '/news/:id', component: NewsDetailPage},
    {path: '/', component: HomePage}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router