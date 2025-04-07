<template>
  <div class="start-page">
    <div class="welcome-card">
      <h2 class="project-title">Распределенные ИС</h2>
      <h2 class="project-title">Лабораторная работа №2</h2>
      <h3 class="author">Автор: ВПР42, Прокопенко Д.О.</h3>

      <button
      class="next-button"
      :class="{ loading: isLoading }"
      @click="handleNavigation"
      :disabled="isLoading"
      >
        <template v-if="!isLoading">
          Далее
        </template>
      <loader-spinner v-else size="small" />
      </button>
    </div>

    <div v-if="authError" class="error-message">
      {{ authError }}
    </div>
  </div>
</template>

<script lang="ts">
  import { defineComponent, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useStore } from 'vuex';
  import LoaderSpinner from '@/components/ui/LoaderSpinner.vue';

  export default defineComponent({
    name: 'StartPage',
    components: { LoaderSpinner },
    setup() {
      const router = useRouter();
      const store = useStore();

      const isLoading = computed(() => store.getters['auth/isLoading']);
      const authError = computed(() => store.getters['auth/authError']);
      const currentUser = computed(() => store.getters['auth/currentUser']);

      onMounted(async () => {
        try {
          await store.dispatch('auth/checkAuth');
        } catch (error) {
          console.error('Auth check failed:', error);
        }
      });

      const handleNavigation = async () => {
        try {
          const isValid = await store.dispatch('auth/checkAuth');

          if (isValid) {
            router.push('/info');
          } else {
            await store.dispatch('auth/logout');
            router.push('/auth');
          }
        } catch (error) {
          store.commit('auth/setError', 'Ошибка при переходе');
          console.error('Navigation error:', error);
        }
      };

      return {
        isLoading,
        authError,
        currentUser,
        handleNavigation
      };
    }
  });
</script>

<style lang="scss" scoped>
@use "@/styles/variables" as v;

.start-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: v.$base-padding * 2;
  text-align: center;
  background-color: var(--background-color);
  @include v.theme-transition;

  .welcome-card {
    background: var(--surface-color);
    border-radius: v.$border-radius-lg;
    box-shadow: var(--card-shadow);
    padding: v.$base-padding * 3;
    margin-bottom: v.$base-margin * 3;
    min-width: 600px;
    max-width: 600px;
    border: 1px solid var(--input-border);
    @include v.theme-transition;

    @media (max-width: 768px) {
      padding: v.$base-padding * 2;
      margin-bottom: v.$base-margin * 2;
    }
  }

  .project-title {
    font-size: 1.7rem;
    color: var(--text-color);
    font-weight: 600;
    @include v.theme-transition;
  }

  .author {
    margin-top: v.$base-margin * 2;
    font-size: 1.5rem;
    line-height: 1.6;
    color: var(--text-color);
    max-width: 500px;
    // margin: 0 auto;
  }

  .next-button {
    margin-top: v.$base-margin * 2;
    position: relative;
    padding: v.$base-padding;
    font-size: 1.1rem;
    font-weight: 700;
    background-color: var(--primary-color);
    color: white;
    border: none;
    border-radius: v.$border-radius-lg;
    cursor: pointer;
    transition: all v.$transition-duration v.$transition-timing;
    min-width: 150px;

    &:hover:not(:disabled) {
      background-color: var(--secondary-color);
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    &:disabled {
      background-color: var(--button-disabled);
      cursor: not-allowed;
    }

    &.loading {
      opacity: 0.8;
    }
  }

  .error-message {
    margin-top: v.$base-margin * 1.5;
    padding: v.$base-padding;
    background: var(--error-bg);
    color: var(--error-color);
    border-radius: v.$border-radius-md;
    border: 1px solid var(--error-color);
    max-width: 400px;
    width: 100%;
    @include v.theme-transition;
  }
}
</style>