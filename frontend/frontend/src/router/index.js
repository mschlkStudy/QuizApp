// frontend/src/router/index.js

import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import OverviewView from "@/views/OverviewView.vue";
import CreateQuestionView from "@/views/CreateQuestionView.vue";
import PlayAloneView from "@/views/PlayAloneView.vue";
import CoopPlayView from "@/views/CoopPlayView.vue";
import RegisterView from "@/views/RegisterView.vue";

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', name: "Login", component: LoginView },
    { path: '/register', name:"Register", component: RegisterView },
    { path: '/overview', component: OverviewView },
    { path: '/create-question', name: 'CreateQuestion', component: CreateQuestionView },
    { path: '/play-alone', name: "PlayAlone", component: PlayAloneView},
    { path: '/coop-play', name: 'CoopPlay', component: CoopPlayView}
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
