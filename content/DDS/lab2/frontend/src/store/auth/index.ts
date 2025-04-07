import axios from 'axios';
import type { Module } from 'vuex';
import type AuthRequest from '@/interfaces/auth/auth-request';
import type AuthResponse from '@/interfaces/auth/auth-response';
import type RootState from '@/interfaces/state/root-state';
import type User from '@/interfaces/dto/user';
import type AuthState from '@/interfaces/state/auth-state';
import * as authService from '@/services/auth-service';
import * as userService from '@/services/user-service';

const authModule: Module<AuthState, RootState> = {
  namespaced: true,

  state: {
    access_token: localStorage.getItem('access_token'),
    refresh_token: localStorage.getItem('refresh_token'),
    user: null,
    error: null,
    isLoading: false
  },

  mutations: {
    setTokens(state, { access_token, refresh_token }: AuthResponse) {
      state.access_token = access_token;
      state.refresh_token = refresh_token;
      localStorage.setItem('access_token', access_token);
      localStorage.setItem('refresh_token', refresh_token);
    },

    clearAuth(state) {
      state.access_token = null;
      state.refresh_token = null;
      state.user = null;
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
    },

    setUser(state, user: User) {
      state.user = user;
    },

    setError(state, error: string | null) {
      state.error = error;
    },

    setLoading(state, isLoading: boolean) {
      state.isLoading = isLoading;
    }
  },

  actions: {
    async register({ commit, dispatch }, request: AuthRequest) {
      commit('setLoading', true);
      commit('setError', null);

      try {
        const response = await authService.register(request);
        commit('setTokens', response);
        await dispatch('fetchUser');
        return { success: true };
      } catch (error) {
        handleAuthError(commit, error);
        return { success: false };
      } finally {
        commit('setLoading', false);
      }
    },

    async login({ commit, dispatch }, request: AuthRequest) {
      commit('setLoading', true);
      commit('setError', null);
      try {
        const response = await authService.login(request);
        commit('setTokens', response);

        await new Promise(resolve => setTimeout(resolve, 0));

        await dispatch('fetchUser');
        return { success: true };
      } catch (error) {
        handleAuthError(commit, error);
        return { success: false };
      } finally {
        commit('setLoading', false);
      }
    },

    async logout({ commit, state }) {
      commit('setLoading', true);

      try {
        if (state.refresh_token && state.access_token) {
          await authService.logout(state.refresh_token, state.access_token);
        }
      } finally {
        commit('clearAuth');
        commit('setLoading', false);
      }
    },

    async refresh({ commit, state }) {
      if (!state.refresh_token) return null;

      try {
        const response = await authService.refresh(state.refresh_token);
        commit('setTokens', response);
        return response.access_token;
      } catch (error) {
        commit('clearAuth');
        throw error;
      }
    },

    async fetchUser({ commit }) {
      commit('setLoading', true);
      try {
        const user = await userService.fetchUser();
        commit('setUser', user);
        return user;
      } catch (error) {
        commit('setError', 'Failed to fetch user data');
        throw error;
      } finally {
        commit('setLoading', false);
      }
    },

    async checkAuth({ dispatch, state }) {
      if (!state.access_token) return false;

      try {
        await authService.checkAuth(state.access_token);
        await dispatch('fetchUser');
        return true;
      } catch (error) {
        if (axios.isAxiosError(error) && error.response?.status === 401) {
          try {
            await dispatch('refresh');
            return true;
          } catch (refreshError) {
            await dispatch('logout');
            return false;
          }
        }
        return false;
      }
    }
  },

  getters: {
    isAuthenticated: (state) => !!state.access_token,
    currentUser: (state) => state.user,
    authError: (state) => state.error,
    isLoading: (state) => state.isLoading
  }
};

function handleAuthError(commit: any, error: unknown) {
  let errorMessage = 'An error occurred';

  if (axios.isAxiosError(error)) {
    errorMessage = error.response?.data?.message ||
      error.response?.data?.error?.message ||
      error.message;
  } else if (error instanceof Error) {
    errorMessage = error.message;
  }

  commit('setError', errorMessage);
}

export default authModule;