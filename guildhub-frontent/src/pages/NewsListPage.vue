<template>
  <div class="news-list-page">
    <div class="main-content">
      <div class="container">
        <h1>Все новости</h1>
        <div class="news-feed-grid">
          <div v-for="news in newsList" :key="news.id" class="news-card">
            <img v-if="news.imageUrl" :src="news.imageUrl" :alt="news.title" class="news-image" />
            <h3 class="news-title">{{ news.title }}</h3>
            <p class="news-description">{{ truncate(news.content, 120) }}</p>
            <router-link :to="`/news/${news.id}`" class="read-more">Читать далее</router-link>
          </div>
        </div>

        <div class="pagination" v-if="totalPages > 1">
          <button
            :disabled="currentPage === 0"
            @click="loadPage(currentPage - 1)"
            class="btn-pagination"
          >
            ← Предыдущая
          </button>

          <span class="page-info">
            Страница {{ currentPage + 1 }} из {{ totalPages }}
          </span>

          <button
            :disabled="currentPage >= totalPages - 1"
            @click="loadPage(currentPage + 1)"
            class="btn-pagination"
          >
            Следующая →
          </button>
        </div>
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
  name: "NewsListPage",
  data() {
    return {
      newsList: [],
      currentPage: 0,
      totalPages: 1,
      pageSize: 10
    };
  },
  async mounted() {
    await this.loadPage(this.currentPage);
  },
  methods: {
    async loadPage(page) {
      try {
        const response = await fetch(`http://localhost:8080/api/news?page=${page}&size=${this.pageSize}`, {
          headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
          throw new Error(await response.text());
        }

        const data = await response.json();

        this.newsList = data.news || [];
        this.currentPage = data.page || 0;
        this.totalPages = data.totalPages || 1;

      } catch (err) {
        console.error('Ошибка загрузки новостей:', err);
        alert('Не удалось загрузить новости');
      }
    },

    truncate(text, length) {
      if (text.length > length) {
        return text.substring(0, length) + '...';
      }
      return text;
    }
  }
};
</script>

<style scoped>
.news-list-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #0d0d0d;
  color: white;
}

.main-content {
  flex: 1;
  padding: 2rem 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

h1 {
  font-size: 2.25rem;
  text-align: center;
  margin-bottom: 2rem;
  color: white;
}

.news-feed-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.news-card {
  background: rgba(20, 20, 20, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.news-card:hover {
  transform: translateY(-4px);
}

.news-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.news-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 0.75rem 0;
  color: white;
}

.news-description {
  font-size: 0.95rem;
  color: #ccc;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.read-more {
  color: #66d9ff;
  text-decoration: none;
  font-weight: 600;
  display: inline-block;
}

.read-more:hover {
  text-decoration: underline;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2rem;
  padding: 1rem;
  background: rgba(15, 15, 15, 0.8);
  border-radius: 8px;
}

.btn-pagination {
  padding: 0.5rem 1rem;
  background: #66d9ff;
  color: #000;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-pagination:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-info {
  font-size: 1rem;
  color: #aaa;
}

.footer {
  background-color: #0a0a0a;
  padding: 1.5rem 0;
  text-align: center;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.2);
}

.footer p {
  color: #aaa;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .news-feed-grid {
    grid-template-columns: 1fr;
  }

  .pagination {
    flex-direction: column;
    gap: 0.75rem;
  }

  .page-info {
    margin: 0.5rem 0;
  }
}
</style>