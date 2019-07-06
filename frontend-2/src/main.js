import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'

import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import '@/styles/colors.css'
import '@/styles/app.scss'

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')