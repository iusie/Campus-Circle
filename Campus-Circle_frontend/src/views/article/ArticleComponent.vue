<template>
  <div id="articleComponent">
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
      <li v-for="article in articles" :key="article.id" class="infinite-list-item">
        <img :src="article.image" alt="Blog Image" class="article-image" />
        <div class="article-content">
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="article-meta">
            <span class="tags">{{ article.tags.join(', ') }}</span>
            <span class="views">👁️ {{ article.views }}</span>
            <span class="likes">❤️ {{ article.likes }}</span>
            <span class="author">By {{ article.author }}</span>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const articles = ref([
  {
    id: 1,
    image: 'https://via.placeholder.com/150',
    title: 'First Blog Post',
    summary: 'This is a summary of the first blog post.',
    tags: ['Vue', 'JavaScript'],
    views: 120,
    likes: 30,
    author: 'Author One'
  },
  {
    id: 2,
    image: 'https://via.placeholder.com/150',
    title: 'Second Blog Post',
    summary: 'This is a summary of the second blog post.',
    tags: ['CSS', 'Design'],
    views: 95,
    likes: 20,
    author: 'Author Two'
  }
]);

const load = () => {
  // Load more articles logic
  articles.value.push(...[
    {
      id: articles.value.length + 1,
      image: 'https://via.placeholder.com/150',
      title: 'New Blog Post',
      summary: 'This is a summary of a new blog post.',
      tags: ['New', 'Post'],
      views: 50,
      likes: 10,
      author: 'Author Three'
    }
  ]);
}
</script>

<style>
#articleComponent {
  width: 100%;
}

.el-scrollbar__wrap {
  overflow: auto;
  height: 100%;
}

.infinite-list {
  height: 70vh;
  padding: 0;
  margin: 0;
  list-style: none;
}

/* Customize Scrollbar */
.infinite-list::-webkit-scrollbar {
  width: 5px;
}

.infinite-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.infinite-list::-webkit-scrollbar-thumb {
  background: rgba(60, 197, 255, 0.87);
  border-radius: 10px;
}

.infinite-list::-webkit-scrollbar-thumb:hover {
  background: rgba(49, 164, 221, 0.7);
}

.infinite-list-item {
  display: flex;
  align-items: flex-start;
  background: #f5f5f5;
  margin: 10px;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.article-image {
  width: 150px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
  margin-right: 15px; /* Space between image and content */
}

.article-content {
  flex: 1;
}

.article-title {
  font-size: 1.2em;
  margin: 0;
}

.article-summary {
  margin: 5px 0;
}

.article-meta {
  display: flex;
  gap: 10px;
  font-size: 0.9em;
  color: #666;
}

.tags {
  font-weight: bold;
}
</style>
