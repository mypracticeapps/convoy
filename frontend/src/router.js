import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import(/* webpackChunkName: "about" */ './views/repos.vue')
    },
    {
      path: '/repos',
      name: 'repos',
      component: () => import(/* webpackChunkName: "about" */ './views/repos.vue')
    },
    {
      path: '/startup',
      name: 'startup',
      component: () => import(/* webpackChunkName: "about" */ './views/server.startup.vue')
    },
    {
      path: '/error',
      name: 'error',
      component: () => import(/* webpackChunkName: "about" */ './views/error.vue')
    },
    {
      path: '/about',
      name: 'about',
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    }
  ]
})
