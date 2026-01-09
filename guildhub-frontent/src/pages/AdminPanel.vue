<template>
  <div class="admin-panel">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>Admin Panel</h2>
        <router-link to="/" class="back-link">← Back to Home</router-link>
      </div>
      <nav class="sidebar-nav">
        <button
          v-for="section in sections"
          :key="section.id"
          @click="activeSection = section.id"
          :class="['nav-item', { active: activeSection === section.id }]"
        >
          <span class="nav-icon">{{ section.icon }}</span>
          <span class="nav-label">{{ section.label }}</span>
        </button>
      </nav>
    </aside>

    <main class="admin-content">
      <header class="content-header">
        <h1>{{ getCurrentSectionLabel() }}</h1>
        <div class="header-actions">
          <span class="user-info">{{ user.username }}</span>
          <button @click="logout" class="btn-logout">Logout</button>
        </div>
      </header>

      <div class="content-body">
        <VideosManagement v-if="activeSection === 'videos'" />
        <TeamsManagement v-if="activeSection === 'teams'" />
        <PlayersManagement v-if="activeSection === 'players'" />
        <UsersManagement v-if="activeSection === 'users'" />
        <TrainingSessionsManagement v-if="activeSection === 'training'" />
        <MatchResultsManagement v-if="activeSection === 'matches'" />
        <TacticalNotesManagement v-if="activeSection === 'notes'" />
        <StatisticsManagement v-if="activeSection === 'statistics'" />
        <NewsManagement v-if="activeSection === 'news'" />
      </div>
    </main>
  </div>
</template>

<script>
import VideosManagement from '@/components/admin/VideosManagement.vue';
import TeamsManagement from '@/components/admin/TeamsManagement.vue';
import PlayersManagement from '@/components/admin/PlayersManagement.vue';
import UsersManagement from '@/components/admin/UsersManagement.vue';
import TrainingSessionsManagement from '@/components/admin/TrainingSessionsManagement.vue';
import MatchResultsManagement from '@/components/admin/MatchResultsManagement.vue';
import TacticalNotesManagement from '@/components/admin/TacticalNotesManagement.vue';
import StatisticsManagement from '@/components/admin/StatisticsManagement.vue';
import NewsManagement from '@/components/admin/NewsManagement.vue';

export default {
  name: 'AdminPanel',
  components: {
    VideosManagement,
    TeamsManagement,
    PlayersManagement,
    UsersManagement,
    TrainingSessionsManagement,
    MatchResultsManagement,
    TacticalNotesManagement,
    StatisticsManagement,
    NewsManagement
  },
  data() {
    return {
      activeSection: 'news',
      user: {
        username: '',
        roles: []
      },
      sections: [
        { id: 'videos', label: 'Видео', icon: '🎬' },
        { id: 'teams', label: 'Команды', icon: '👥' },
        { id: 'players', label: 'Игроки', icon: '🎮' },
        { id: 'users', label: 'Пользователи', icon: '👤' },
        { id: 'training', label: 'Тренировки', icon: '🏋️' },
        { id: 'matches', label: 'Матчи', icon: '⚔️' },
        { id: 'notes', label: 'Заметки', icon: '📝' },
        { id: 'statistics', label: 'Статистика', icon: '📊' },
        { id: 'news', label: 'Новости', icon: '📰' }
      ]
    };
  },
  mounted() {
    this.checkAuth();
    this.loadUserData();
  },
  methods: {
    checkAuth() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.$router.push('/login');
        return;
      }

      const roles = JSON.parse(localStorage.getItem('roles') || '[]');
      const isAdmin = roles.includes('ADMIN');
      
      if (!isAdmin) {
        alert('У вас нет доступа к админ панели');
        this.$router.push('/');
      }
    },
    loadUserData() {
      this.user.username = localStorage.getItem('username') || '';
      this.user.roles = JSON.parse(localStorage.getItem('roles') || '[]');
    },
    getCurrentSectionLabel() {
      const section = this.sections.find(s => s.id === this.activeSection);
      return section ? section.label : 'Admin Panel';
    },
    logout() {
      localStorage.clear();
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

.admin-panel {
  display: flex;
  min-height: 100vh;
  background-color: #0f0f0f;
  color: white;
  font-family: 'Roboto', sans-serif;
}

.sidebar {
  width: 280px;
  background-color: #0a0a0a;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  height: 100vh;
  overflow-y: auto;
}

.sidebar-header {
  padding: 2rem 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header h2 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  color: #66d9ff;
}

.back-link {
  color: #aaa;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.2s;
}

.back-link:hover {
  color: #66d9ff;
}

.sidebar-nav {
  padding: 1rem 0;
  flex: 1;
}

.nav-item {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background: transparent;
  border: none;
  color: #ccc;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 1rem;
  font-family: 'Roboto', sans-serif;
}

.nav-item:hover {
  background-color: rgba(102, 217, 255, 0.1);
  color: #66d9ff;
}

.nav-item.active {
  background-color: rgba(102, 217, 255, 0.2);
  color: #66d9ff;
  border-left: 3px solid #66d9ff;
}

.nav-icon {
  font-size: 1.2rem;
}

.nav-label {
  font-weight: 500;
}

.admin-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-header {
  padding: 1.5rem 2rem;
  background-color: #121212;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-header h1 {
  font-size: 1.8rem;
  font-weight: 700;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info {
  color: #aaa;
  font-size: 0.9rem;
}

.btn-logout {
  padding: 0.5rem 1rem;
  background: rgba(255, 107, 107, 0.2);
  color: #ff6b6b;
  border: 1px solid rgba(255, 107, 107, 0.3);
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s;
  font-family: 'Roboto', sans-serif;
}

.btn-logout:hover {
  background: rgba(255, 107, 107, 0.3);
  border-color: rgba(255, 107, 107, 0.5);
}

.content-body {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .admin-panel {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
  }

  .sidebar-nav {
    display: flex;
    overflow-x: auto;
    padding: 0.5rem;
  }

  .nav-item {
    min-width: 120px;
    padding: 0.75rem 1rem;
  }

  .nav-label {
    font-size: 0.85rem;
  }
}
</style>