import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'

import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '@/styles/shards.css'
import '@/styles/colors.css'
import '@/styles/app.scss'
import '@/styles/home-page.scss'

import '@/services/api.client';

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')