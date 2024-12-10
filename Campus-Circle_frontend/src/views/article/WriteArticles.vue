<template>
  <!-- 指定一个容器 -->
  <div id="vditor"></div>
</template>

<script setup lang="ts">
// 1.1 引入Vditor 构造函数
import Vditor from 'vditor'
// 1.2 引入样式
import 'vditor/dist/index.css'
import { ref, onMounted } from 'vue'

// 2. 获取DOM引用
const vditor = ref()

// 3. 在组件初始化时，就创建Vditor对象，并引用
onMounted(() => {
  vditor.value = new Vditor('vditor', {
    height: '50vh',
    width: '50vw'
  })
})

</script>

<style>
button[data-type="preview"]
, button[data-type="help"]
, button[data-type="info"]
, button[data-type="devtools"]
, button[data-type="tablet"]
, button[data-type="mobile"]
, button[data-type="mp-wechat"]
, button[data-type="zhihu"]
, button[data-type="desktop"]{
  display: none;
}
</style>


