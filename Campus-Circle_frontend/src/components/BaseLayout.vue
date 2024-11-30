<template>
  <div class="common-layout">
    <HeaderView />
    <MainBodyView />
    <FooterView />
  </div>
</template>

<script setup lang="ts">
import HeaderView from '@/components/HeaderView.vue'
import MainBodyView from '@/components/MainBodyView.vue'
import FooterView from '@/components/FooterView.vue'
</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
}
</style>
