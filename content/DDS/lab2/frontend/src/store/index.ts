import { createStore } from "vuex";
import type RootState from "@/interfaces/state/root-state";
import authModule from "@/store/auth";

export default createStore<RootState>({
  modules: {
    auth: authModule
  }
});