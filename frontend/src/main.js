import Vue from 'vue'
import VueRx from 'vue-rx'

import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'

import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '@/assets/fontawesome/css/all.css'
import '@/styles/shards.css'
import '@/styles/colors.css'
import '@/styles/app.scss'
import '@/styles/home-page.scss'
import '@/styles/repos.scss'
import '@/styles/repo.scss'
import '@/styles/commit.scss'


import '@/services/api.client';

import _ from 'lodash';
Object.defineProperty(Vue.prototype, '$_', { value: _ });

Vue.config.productionTip = false;
Vue.use(VueRx);

import Toasted from 'vue-toasted';
Vue.use(Toasted);

import {http, httpConfig} from "@/services/api.client"
httpConfig(router);

let app = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
