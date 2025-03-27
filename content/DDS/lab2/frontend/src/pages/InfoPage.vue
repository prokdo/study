<template>
    <div class="info-page">
      <header class="info-header">
        <div class="user-info">
          <span class="username">{{ formattedUsername }}</span>
          <button
            class="logout-button"
            @click="handleLogout"
            :disabled="isLoading"
          >
            <span v-if="!isLoading">Выход</span>
            <div v-else class="spinner"></div>
          </button>
        </div>
      </header>

      <main class="info-content">
        <h2 class="content-title">О проекте</h2>
        <div class="content-text">
          <p>Добро пожаловать в систему управления проектами!</p>
          <ul class="features-list">
            <li>Управление задачами в режиме реального времени</li>
            <li>Совместная работа в команде</li>
            <li>Аналитика и отчетность</li>
            <li>Интеграция с внешними сервисами</li>
          </ul>
        </div>
      </main>
    </div>
  </template>

  <script lang="ts">
  import { defineComponent, computed } from 'vue';
  import { useStore } from 'vuex';
  import { useRouter } from 'vue-router';

  export default defineComponent({
    name: 'InfoPage',
    setup() {
      const store = useStore();
      const router = useRouter();

      const currentUser = computed(() => store.getters['auth/currentUser']);
      const isLoading = computed(() => store.getters['auth/isLoading']);

      const formattedUsername = computed(() => {
        const username = currentUser.value?.username;
        return username
          ? username.charAt(0).toUpperCase() + username.slice(1)
          : 'Пользователь';
      });

      const handleLogout = async () => {
        try {
          await store.dispatch('auth/logout');
          router.push('/auth');
        } catch (error) {
          console.error('Ошибка при выходе:', error);
        }
      };

      return {
        formattedUsername,
        isLoading,
        handleLogout
      };
    }
  });
  </script>

  <style lang="scss" scoped>
  .info-page {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
  }

  .info-header {
    border-bottom: 2px solid #e4e7eb;
    margin-bottom: 2rem;
    padding-bottom: 1rem;

    .user-info {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .username {
        font-size: 1.25rem;
        font-weight: 600;
        color: #2d3748;
      }
    }
  }

  .logout-button {
    padding: 0.5rem 1.5rem;
    background: #f56565;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s ease;
    position: relative;

    &:hover:not(:disabled) {
      background: #c53030;
    }

    &:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
  }

  .info-content {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .content-title {
      font-size: 1.75rem;
      color: #2d3748;
      margin-bottom: 1.5rem;
    }

    .content-text {
      line-height: 1.6;
      color: #4a5568;

      p {
        margin-bottom: 1rem;
      }
    }
  }

  .features-list {
    padding-left: 1.5rem;
    list-style-type: circle;

    li {
      margin-bottom: 0.75rem;
      padding-left: 0.5rem;
    }
  }

  .spinner {
    display: inline-block;
    width: 20px;
    height: 20px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top-color: white;
    animation: spin 1s linear infinite;
    margin: 0 auto;
  }

  @keyframes spin {
    to { transform: rotate(360deg); }
  }
  </style>