<template>
    <div class="start-page">
      <h1 class="project-title">{{ projectName }}</h1>

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

      const projectName = 'Test'
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
        projectName,
        isLoading,
        authError,
        currentUser,
        handleNavigation
      };
    }
  });
</script>

<style lang="scss" scoped>
  .start-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 2rem;
    text-align: center;

    .project-title {
      font-size: 2.5rem;
      margin-bottom: 2rem;
      color: #2c3e50;
      font-weight: 600;
    }

    .next-button {
      position: relative;
      padding: 1rem 3rem;
      font-size: 1.1rem;
      background-color: #42b983;
      color: white;
      border: none;
      border-radius: 30px;
      cursor: pointer;
      transition: all 0.3s ease;
      min-width: 150px;

      &:hover:not(:disabled) {
        background-color: #33a06f;
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
      }

      &:disabled {
        background-color: #cccccc;
        cursor: not-allowed;
      }

      &.loading {
        opacity: 0.8;
      }
    }

    .error-message {
      margin-top: 1.5rem;
      padding: 1rem;
      background-color: #ffeaea;
      color: #ff4444;
      border-radius: 8px;
      border: 1px solid #ffcccc;
      max-width: 400px;
      width: 100%;
    }
  }
</style>