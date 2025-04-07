<template>
    <div class="info-card">
      <img :src="iconPath" alt="icon" class="card-icon" loading="lazy">
      <h3 class="card-title">{{ title }}</h3>
      <p lang="ru" class="card-description">{{ description }}</p>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { PropType } from 'vue';
import type InfoCardProps from '@/interfaces/props/InfoCardProps';

export default defineComponent({
  name: 'InfoCard',
  props: {
    iconPath: {
      type: String as PropType<string>,
      required: true,
      default: 'icon'
    },
    title: {
      type: String as PropType<string>,
      required: true,
      default: 'title'
    },
    description: {
      type: String as PropType<string>,
      required: true,
      default: 'description'
    }
  },
  setup(props: InfoCardProps) {
    return { props };
  }
});
</script>

<style lang="scss" scoped>
@use "@/styles/variables" as v;

.info-card {
    background: var(--surface-color);
    border-radius: v.$border-radius-lg;
    box-shadow: var(--card-shadow);
    padding: v.$base-padding * 2;
    margin: v.$base-margin;
    max-width: 300px;
    border: 1px solid var(--input-border);
    transition: transform 0.3s v.$transition-timing,
                box-shadow 0.3s v.$transition-timing;
    text-align:center;
    min-width: 260px;
    min-height: 500px;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
    }
  }

  .card-icon {
  width: 64px;
  height: 64px;
  object-fit: contain;
  margin: 0 auto 1rem;
  border-radius: 12px;
  // box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.05);
  }
}

  .card-title {
    color: var(--text-color);
    font-size: 1.4rem;
    margin-bottom: v.$base-margin;
    font-weight: 600;
  }

  .card-description {
    text-align: justify;
    color: var(--text-color);
    line-height: 1.6;
    hyphens: auto;
    margin: 0 auto;
    max-width: 24ch;
    hyphens: auto;
    text-align-last: center;
  }
</style>