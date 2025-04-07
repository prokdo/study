<template>
    <div class="auth-page">
      <div class="auth-container">
        <div class="auth-title">
          <h3>Распределенные ИС</h3>
          <h3>Лабораторная работа №2</h3>
        </div>
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
                <label for="username">Имя пользователя</label>
            <input
                id="username"
                v-model="username"
                type="text"
                required
                :disabled="isLoading"
            />
            </div>

          <div class="input-group">
            <label for="password">Пароль</label>
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
@use "@/styles/variables" as v;

.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: v.$base-padding * 1.25;
  background-color: var(--background-color);
  @include v.theme-transition;
}

.auth-container {
  background: var(--surface-color);
  border: 1px solid var(--input-border);
  border-radius: v.$border-radius-lg;
  box-shadow: var(--card-shadow);
  padding: v.$base-padding * 2;
  width: 100%;
  max-width: 420px;
  @include v.theme-transition;

  .auth-title {
    font-weight: 600;
    margin-bottom: v.$base-margin * 2;
    color: var(--text-color);
    @include v.theme-transition;
  }
}

.mode-toggle {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: v.$base-padding * 0.5;
  margin-bottom: v.$base-margin * 2;

  .toggle-button {
    padding: v.$base-padding;
    border: none;
    background: var(--button-secondary-bg);
    color: var(--text-color);
    cursor: pointer;
    border-radius: v.$border-radius-md;
    font-weight: 700;
    @include v.theme-transition;

    &.active {
      background: var(--primary-color);
      color: rgb(255, 255, 255);
    }

    &:hover:not(.active) {
      background: var(--primary-color);
      color: white
    }
  }
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: v.$base-padding * 1.5;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: v.$base-padding * 0.5;

  label {
    font-weight: 500;
    color: var(--text-color);
    text-align: left;
  }

  input {
    padding: v.$base-padding;
    border: 1px solid var(--input-border);
    border-radius: v.$border-radius-md;
    font-size: 1rem;
    background: var(--input-background);
    color: var(--text-color);
    @include v.theme-transition;

    &:focus {
      outline: none;
      border-color: var(--primary-color);
      box-shadow: 0 0 0 2px rgba(var(--primary-color), 0.2);
    }

    &:disabled {
      background: var(--input-disabled);
      cursor: not-allowed;
    }
  }
}

.submit-button {
  padding: v.$base-padding;
  background: var(--primary-color);
  color: rgb(255, 255, 255);
  border: none;
  border-radius: v.$border-radius-md;
  font-weight: 700;
  cursor: pointer;
  height: 44px;
  transition: all v.$transition-duration v.$transition-timing;

  &:hover:not(:disabled) {
    background: var(--secondary-color);
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  }

  &:disabled {
    background: var(--button-disabled);
    cursor: not-allowed;
  }
}

.error-message {
  padding: v.$base-padding;
  background: var(--error-bg);
  color: var(--error-color);
  border-radius: v.$border-radius-md;
  border: 1px solid var(--error-color);
  font-size: 0.9rem;
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid var(--spinner-border);
  border-radius: 50%;
  border-top-color: var(--spinner-color);
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>