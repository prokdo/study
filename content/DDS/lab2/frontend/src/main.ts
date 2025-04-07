import { createApp } from 'vue'
import '@/style.scss'
import App from '@/app/App.vue'
import store from './store'
import router from './router'

const app = createApp(App)
app.config.performance = true
app.use(store)
app.use(router)
app.mount('#app')