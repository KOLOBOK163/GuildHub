<template>
  <div class="hl-tv">
    <main class="main-content">
      <section class="hero">
        <div class="container hero-content">
          <h1>Welcome to GuildHub</h1>
          <p>Your ultimate source for competitive gaming news, tournaments, and results.</p>
          <router-link to="/news" class="btn-primary">Explore Now</router-link>
        </div>
      </section>

      <section class="section news">
        <div class="container">
          <h2>News Feed</h2>
          <div v-if="loadingNews" class="news-loading">Loading latest news...</div>
          <div v-else-if="newsFeed.length === 0" class="news-empty">No news available.</div>
          <div v-else class="news-feed-list">
            <router-link
              v-for="news in newsFeed"
              :key="news.id"
              :to="`/news/${news.id}`"
              class="news-item-link"
            >
              <div class="news-item">
                <img v-if="news.imageUrl" :src="news.imageUrl" :alt="news.title" class="news-item-image" />
                <h3 class="news-item-title">{{ news.title }}</h3>
              </div>
            </router-link>
          </div>
        </div>
      </section>

      <section class="section teams">
        <div class="container">
          <h2>Top Teams</h2>
          <div class="team-grid">
            <div v-for="team in topTeams" :key="team.id" class="team-card">
              <img :src="team.logo" :alt="team.name" class="team-logo" />
              <h3>{{ team.name }}</h3>
            </div>
          </div>
        </div>
      </section>

      <section class="section players">
        <div class="container">
          <h2>Top Players</h2>
          <div class="player-grid">
            <div v-for="player in topPlayers" :key="player.id" class="player-card">
              <img :src="player.avatar" :alt="player.name" class="player-avatar" />
              <h3>{{ player.name }}</h3>
            </div>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <div class="container">
        <p>&copy; 2025 GuildHub. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: "HomePage",
  data() {
    return {
      topTeams: [
        { id: 1, logo: '/team1.png', name: 'G2 Esports' },
        { id: 2, logo: '/team2.png', name: 'FaZe Clan' },
        { id: 3, logo: '/team3.png', name: 'Natus Vincere' },
        { id: 4, logo: '/team4.png', name: 'Team Liquid' },
      ],
      topPlayers: [
        { id: 1, avatar: '/player1.png', name: 's1mple' },
        { id: 2, avatar: '/player2.png', name: 'ZywOo' },
        { id: 3, avatar: '/player3.png', name: 'NiKo' },
        { id: 4, avatar: '/player4.png', name: 'sh1ro' },
      ],
      newsFeed: [],
      loadingNews: true
    };
  },
  async mounted() {
    await this.loadLatestNews();
  },
  methods: {
    async loadLatestNews() {
      try {
        const response = await fetch('http://localhost:8080/api/home/news?limit=5', {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
          const data = await response.json();
          this.newsFeed = data.news || [];
        } else {
          this.newsFeed = [];
        }
      } catch (err) {
        console.error('Failed to load news:', err);
        this.newsFeed = [];
      } finally {
        this.loadingNews = false;
      }
    }
  }
};
</script>

<style scoped>
.hl-tv {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.main-content {
  flex: 1;
}

.hero {
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  padding: 5rem 0;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 20% 30%, rgba(0, 0, 0, 0.3) 0%, transparent 20%),
              radial-gradient(circle at 80% 70%, rgba(0, 0, 0, 0.3) 0%, transparent 20%);
  pointer-events: none;
}

.hero-content h1 {
  font-size: 3.5rem;
  font-weight: 700;
  color: white;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  margin-bottom: 1rem;
}

.hero-content p {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.85);
  max-width: 600px;
  margin: 0 auto 2rem;
  line-height: 1.6;
}

.btn-primary {
  padding: 1rem 2rem;
  background: #66d9ff;
  color: #000;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  box-shadow: 0 4px 12px rgba(102, 217, 255, 0.3);
}

.btn-primary:hover {
  background: #4ecdc4;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(78, 205, 196, 0.4);
}

.section {
  padding: 4rem 0;
  background-color: #121212;
  text-align: center;
}

.section h2 {
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 2rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.team-grid,
.player-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.team-card,
.player-card {
  background: rgba(30, 30, 30, 0.6);
  border-radius: 12px;
  padding: 1.5rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.team-card:hover,
.player-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  background: rgba(40, 40, 40, 0.8);
}

.team-logo,
.player-avatar {
  width: 100px;
  height: 100px;
  object-fit: contain;
  border-radius: 8px;
  margin-bottom: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.team-card h3,
.player-card h3 {
  font-size: 1.2rem;
  font-weight: 600;
  color: white;
  margin: 0;
}

.news-feed-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-top: 1.5rem;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.news-item-link {
  text-decoration: none;
  color: inherit;
  display: block;
}

.news-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1rem;
  background: rgba(30, 30, 30, 0.6);
  border-radius: 10px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.news-item:hover {
  background: rgba(40, 40, 40, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
}

.news-item-image {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.news-item-title {
  font-size: 1.05rem;
  font-weight: 600;
  color: white;
  margin: 0;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  flex-grow: 1;
  min-width: 0;
}

.news-loading,
.news-empty {
  text-align: center;
  color: #aaa;
  padding: 1.5rem;
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
  .hero-content h1 { font-size: 2.5rem; }
  .hero-content p { font-size: 1rem; }
  .btn-primary { padding: 0.75rem 1.5rem; font-size: 0.95rem; }
  .section h2 { font-size: 2rem; }
  .team-grid,
  .player-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
  .news-item-image {
    width: 60px;
    height: 60px;
  }
  .news-item-title {
    font-size: 1rem;
  }
}
</style>