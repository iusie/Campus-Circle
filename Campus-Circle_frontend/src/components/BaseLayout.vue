<template>
  <div class="common-layout">
    <HeaderView />
    <RouterView />
  </div>
</template>

<script setup lang="ts">
import HeaderView from '@/components/HeaderView.vue'
</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
}
</style>
