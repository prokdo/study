<template>
    <div class="loader" :class="[size]">
        <div class="loader-spinner"></div>
    </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import type { PropType } from 'vue';

  export default defineComponent({
    name: 'LoaderSpinner',
    props: {
      size: {
        type: String as PropType<'small' | 'medium' | 'large'>,
        default: 'medium'
      }
    }
  });
</script>

<style lang="scss" scoped>
  .loader {
    display: inline-block;
    position: relative;

    &-spinner {
      animation: rotate 1s linear infinite;
      border-style: solid;
      border-radius: 50%;
      border-top-color: transparent;
      margin: auto;

      .small & {
        width: 20px;
        height: 20px;
        border-width: 2px;
      }

      .medium & {
        width: 30px;
        height: 30px;
        border-width: 3px;
      }

      .large & {
        width: 40px;
        height: 40px;
        border-width: 4px;
      }
    }
  }

  @keyframes rotate {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
</style>