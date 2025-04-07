<template>
  <div class="info-page">
    <header class="info-header">
      <div class="user-info">
        <span class="username">Добро пожаловать, {{ formattedUsername }}</span>
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
      <h2 class="content-title">Используемые технологии</h2>

      <div class="cards-container">
        <InfoCard
          v-for="(tech, index) in technologies"
          :key="index"
          :iconPath="tech.iconPath"
          :title="tech.title"
          :description="tech.description"
        />
      </div>
    </main>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import InfoCard from '@/components/content/InfoCard.vue';
import type InfoCardProps from '@/interfaces/props/InfoCardProps';

export default defineComponent({
  name: 'InfoPage',
  components: {
    InfoCard
  },
  setup() {
    const store = useStore();
    const router = useRouter();

    const currentUser = computed(() => store.getters['auth/currentUser']);
    const isLoading = computed(() => store.getters['auth/isLoading']);

    const formattedUsername = computed(() => {
      return currentUser.value?.username || 'пользователь';
    });

    const handleLogout = async () => {
      try {
        await store.dispatch('auth/logout');
        await router.push('/auth');
      } catch (error) {
        console.error('Ошибка при выходе:', error);
      }
    };

    const technologies: InfoCardProps[] = [
      {
        iconPath: '/icons/docker.svg',
        title: 'Docker',
        description: 'Программное обеспечение для автоматизации развертывания и управления приложениями в среде виртуализации. Предоставляет инструмент Docker Compose, предназначенный для компоновки, запуска и управления мультиконтейнерыми приложениями'
      },
      {
        iconPath: '/icons/postgresql.svg',
        title: 'PostgreSQL',
        description: 'Объектно-реляционная СУБД с открытым исходным кодом, обеспечивающая надежное хранение и целостность данных. Обладает большим сообществом пользователей, является наиболее развитой открытой СУБД, имеет поддержку JSON и высокую мощность'
      },
      {
        iconPath: '/icons/go.svg',
        title: 'Go',
        description: 'Компилируемый статически типизированный язык программирования, разработанный Google. Имеет простой синтаксис, обладает высокой производительностью, эффективным и безопасным управлением памятью и облегченным API для параллельных вычислений'
      },
      {
        iconPath: '/icons/vue.svg',
        title: 'Vue',
        description: 'Прогрессивный фреймворк для создания пользовательских интерфейсов. Используется для создания динамических одностраничных веб-приложений (SPA), ориентирован на разработку с использованием TypeScript и компонентного подхода'
      },
      {
        iconPath: '/icons/nginx.svg',
        title: 'Nginx',
        description: 'Высокопроизводительный HTTP-сервер и обратный прокси. Является наиболее популярным веб-сервером в российском сегменте сети Интернет, обладает возможностью распределения нагрузки на серверную часть приложения и SSL/TLS-терминацией'
      }
    ];

    return {
      formattedUsername,
      isLoading,
      handleLogout,
      technologies
    };
  }
});
</script>

<style lang="scss" scoped>
@use "@/styles/variables" as v;

.info-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--background-color);
  @include v.theme-transition;
}

.info-header {
  margin-top: v.$base-margin * 2;
  padding: v.$base-padding * 2;
  background-color: var(--surface-color);
  box-shadow: var(--card-shadow);
  border-radius: v.$border-radius-md;
  border: 1px solid var(--input-border);

  .user-info {
    max-width: 1280px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.username {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-color);
}

.logout-button {
  padding: v.$base-padding v.$base-padding * 2;
  background-color: var(--primary-color);
  color: white;
  border: 1px solid var(--error-border);
  font-weight: 700;
  border-radius: v.$border-radius-md;
  cursor: pointer;
  transition: all v.$transition-duration v.$transition-timing;

  &:hover:not(:disabled) {
    background-color: var(--error-color);
    outline: 2px solid var(--error-color);
    outline-offset: 2px;
  }

  &:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }
}

.info-content {
  flex: 1;
  padding: v.$base-padding * 1;
  max-width: 1280px;
  margin: 0 auto;
  width: 100%;
}

.content-title {
  text-align: center;
  font-size: 2rem;
  color: var(--text-color);
  margin-top: v.$base-margin * 2;
  margin-bottom: v.$base-margin * 2;
}

.cards-container {
  display: flex;
  gap: v.$base-margin * 2;
  justify-content: center;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid var(--spinner-border);
  border-radius: 50%;
  border-top-color: var(--spinner-color);
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>