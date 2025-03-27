<template>
  <div id="app">
    <!-- Глобальный обработчик состояния загрузки -->
    <div v-if="isGlobalLoading" class="global-loader">
      <div class="loader-content">
        <div class="spinner"></div>
        <p>Загрузка...</p>
      </div>
    </div>

    <!-- Основной контент приложения -->
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>

    <!-- Глобальные уведомления -->
    <div class="global-notifications">
      <transition-group name="slide">
        <div
          v-for="notification in notifications"
          :key="notification.id"
          :class="['notification', notification.type]"
        >
          {{ notification.message }}
        </div>
      </transition-group>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import { useStore } from 'vuex';

export default defineComponent({
  name: 'App',
  setup() {
    const store = useStore();

    // Глобальные состояния
    const isGlobalLoading = computed(() => store.getters['auth/isLoading']);
    const notifications = computed(() => store.state.notifications);

    return {
      isGlobalLoading,
      notifications
    };
  }
});
</script>

<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.global-loader {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;

  .loader-content {
    text-align: center;

    .spinner {
      width: 50px;
      height: 50px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #42b983;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin: 0 auto 1rem;
    }

    p {
      font-size: 1.2rem;
      color: #2c3e50;
    }
  }
}

.global-notifications {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;

  .notification {
    padding: 1rem 1.5rem;
    border-radius: 8px;
    margin-bottom: 1rem;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
    min-width: 250px;

    &.error {
      background: #fff5f5;
      border: 1px solid #fc8181;
      color: #c53030;
    }

    &.success {
      background: #f0fff4;
      border: 1px solid #68d391;
      color: #2f855a;
    }
  }
}

// Анимации
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>