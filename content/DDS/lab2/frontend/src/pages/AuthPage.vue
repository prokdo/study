<template>
    <div class="auth-page">
      <div class="auth-container">
        <div class="mode-toggle">
          <button
            class="toggle-button"
            :class="{ active: authMode === 'login' }"
            @click="switchMode('login')"
          >
            Вход
          </button>
          <button
            class="toggle-button"
            :class="{ active: authMode === 'register' }"
            @click="switchMode('register')"
          >
            Регистрация
          </button>
        </div>

        <form class="auth-form" @submit.prevent="handleAuthSubmit">
            <div class="input-group">
                <label for="username">Имя пользователя:</label>
            <input
                id="username"
                v-model="username"
                type="text"
                required
                :disabled="isLoading"
            />
            </div>

          <div class="input-group">
            <label for="password">Пароль:</label>
            <input
              id="password"
              v-model="password"
              type="password"
              required
              :disabled="isLoading"
            />
          </div>

          <div v-if="showError" class="error-message">
            {{ errorMessage }}
          </div>

          <button type="submit" class="submit-button" :disabled="isLoading">
            <span v-if="!isLoading">{{ submitButtonText }}</span>
            <div v-else class="spinner"></div>
          </button>
        </form>
      </div>
    </div>
</template>

<script lang="ts">
  import { defineComponent, ref, computed, onMounted } from 'vue';
  import { useStore } from 'vuex';
  import { useRoute, useRouter } from 'vue-router';

  export default defineComponent({
    name: 'AuthPage',
    setup() {
      const store = useStore();
      const router = useRouter();
      const route = useRoute();

      const authMode = ref<'login' | 'register'>('login');
      const username = ref('');
      const password = ref('');

      const isLoading = computed(() => store.getters['auth/isLoading']);
      const authError = computed(() => store.getters['auth/authError']);

      const routeError = ref('');
      const showError = computed(() => routeError.value || authError.value);
      const errorMessage = computed(() => routeError.value || authError.value);

      onMounted(() => {
        if (route.query.error === 'forbidden') {
          routeError.value = 'Доступ запрещен. Требуется авторизация';
        }
        store.commit('auth/setError', null);
      });

      const switchMode = (mode: 'login' | 'register') => {
        authMode.value = mode;
        store.commit('auth/setError', null);
        routeError.value = '';
      };

      const handleAuthSubmit = async () => {
        store.commit('auth/setError', null);
        routeError.value = '';

        const credentials = {
          username: username.value,
          password: password.value
        };

        const action = authMode.value === 'login' ? 'auth/login' : 'auth/register';
        const result = await store.dispatch(action, credentials);

        if (result?.success) {
          const redirectPath = route.query.redirect?.toString() || '/';
          router.push(redirectPath);
        }
      };

      const submitButtonText = computed(() =>
        authMode.value === 'login' ? 'Войти' : 'Зарегистрироваться'
      );

      return {
        authMode,
        username,
        password,
        isLoading,
        showError,
        errorMessage,
        submitButtonText,
        switchMode,
        handleAuthSubmit
      };
    }
  });
</script>

<style lang="scss" scoped>
  .auth-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background: #f5f7fb;
    padding: 20px;
  }

  .auth-container {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 2rem;
    width: 100%;
    max-width: 400px;
  }

  .mode-toggle {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 8px;
    margin-bottom: 2rem;

    .toggle-button {
      padding: 12px;
      border: none;
      background: #f0f2f5;
      cursor: pointer;
      border-radius: 6px;
      font-weight: 500;
      transition: all 0.2s ease;

      &.active {
        background: #42b983;
        color: white;
      }

      &:hover:not(.active) {
        background: #e4e6eb;
      }
    }
  }

  .auth-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }

  .input-group {
    display: flex;
    flex-direction: column;
    gap: 8px;

    label {
      font-weight: 500;
      color: #2d3748;
    }

    input {
      padding: 12px;
      border: 1px solid #e2e8f0;
      border-radius: 6px;
      font-size: 1rem;
      transition: border-color 0.2s ease;

      &:focus {
        outline: none;
        border-color: #42b983;
        box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
      }

      &:disabled {
        background: #f8f9fa;
        cursor: not-allowed;
      }
    }
  }

  .submit-button {
    padding: 12px;
    background: #42b983;
    color: white;
    border: none;
    border-radius: 6px;
    font-weight: 500;
    cursor: pointer;
    transition: background 0.2s ease;
    height: 44px;

    &:hover:not(:disabled) {
      background: #38a169;
    }

    &:disabled {
      background: #a0aec0;
      cursor: not-allowed;
    }
  }

  .error-message {
    padding: 12px;
    background: #fff5f5;
    color: #f56565;
    border-radius: 6px;
    border: 1px solid #fed7d7;
    font-size: 0.9rem;
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