<template>
  <div class="news-detail-page">
    <div class="container">
      <div v-if="loading" class="loading">Загрузка...</div>

      <div v-else-if="error" class="error">
        {{ error }}
      </div>

      <div v-else class="news-card">
        <h1 class="news-title">{{ news.title }}</h1>

        <div v-if="news.imageUrl" class="news-image-wrapper">
          <img :src="news.imageUrl" :alt="news.title" class="news-image" />
        </div>

        <div class="news-meta">
          <span class="author">Автор: {{ news.author }}</span>
          <span class="date">Дата: {{ formatDate(news.createdAt) }}</span>
        </div>

        <div class="news-content">
          <p>{{ news.content }}</p>
        </div>

        <router-link to="/news" class="back-link">← Назад к новостям</router-link>
      </div>
    </div>

    <footer class="footer">
      <div class="container">
        <p>&copy; 2025 GuildHub. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: "NewsDetailPage",
  data() {
    return {
      news: {},
      loading: true,
      error: null
    };
  },
  async mounted() {
    const newsId = this.$route.params.id;
    if (!newsId) {
      this.error = 'ID новости не указан';
      this.loading = false;
      return;
    }

    await this.loadNews(newsId);
  },
  methods: {
    async loadNews(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/news/${id}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
          const data = await response.json();

          this.news = data.news || {};
        } else {
          const errorText = await response.text();
          this.error = 'Ошибка загрузки новости: ' + errorText;
        }
      } catch (err) {
        this.error = 'Произошла ошибка при загрузке новости. Попробуйте позже.';
        console.error('Network or unexpected error:', err);
      } finally {
        this.loading = false;
      }
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
.news-detail-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #0d0d0d;
  color: white;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

.loading,
.error {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #aaa;
}

.news-card {
  background: rgba(20, 20, 20, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2.5rem;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
}

.news-title {
  font-size: 2.25rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: white;
  line-height: 1.3;
}

.news-image-wrapper {
  text-align: center;
  margin: 1.5rem 0;
}

.news-image {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.3);
  object-fit: cover;
}

.news-meta {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
  color: #aaa;
}

.news-content p {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #e0e0e0;
  margin: 0;
}

.back-link {
  display: inline-block;
  margin-top: 1.5rem;
  color: #66d9ff;
  text-decoration: none;
  font-weight: 600;
}

.back-link:hover {
  text-decoration: underline;
}

.footer {
  background-color: #0a0a0a;
  padding: 1.5rem 0;
  text-align: center;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.2);
  margin-top: auto;
}

.footer p {
  color: #aaa;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .container {
    padding: 1.5rem;
  }

  .news-card {
    padding: 1.75rem;
  }

  .news-title {
    font-size: 1.75rem;
  }

  .news-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>