import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import(/* webpackChunkName: "about" */ './views/Repos.vue')
    },
    {
      path: '/repos',
      name: 'repos',
      component: () => import(/* webpackChunkName: "about" */ './views/Repos.vue')
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
});

router.beforeResolve((to, from, next) => {
  // If this isn't an initial page load.
  if (to.name) {
    // Start the route progress bar.
    NProgress.start()
  }
  next()
});

router.afterEach((to, from) => {
  // Complete the animation of the route progress bar.
  NProgress.done()
});

export default router;
