import { createRouter, createWebHistory } from 'vue-router'
import type { Router, RouteRecordRaw } from 'vue-router'
import store from '@/store'

const AuthPage = () => import('@/pages/AuthPage.vue')
const InfoPage = () => import('@/pages/InfoPage.vue')
const StartPage = () => import('@/pages/StartPage.vue')

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'StartPage',
    component: StartPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/auth',
    name: 'AuthPage',
    component: AuthPage,
    meta: { requiresAuth: false }
  },
  {
    path: '/info',
    name: 'InfoPage',
    component: InfoPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/auth'
  }
]

const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach(async (to, _from, next) => {
  const isAuthRoute = to.name === 'AuthPage';
  const requiresAuth = to.meta.requiresAuth;

  try {
    const isAuthenticated = await store.dispatch('auth/checkAuth');

    if (requiresAuth && !isAuthenticated) {
      next({
        path: '/auth',
        query: { redirect: to.fullPath }
      });
    } else if (isAuthRoute && isAuthenticated) {
      next('/');
    } else {
      next();
    }
  } catch (error) {
    console.error('Navigation error:', error);
    next('/auth');
  }
});

export default router