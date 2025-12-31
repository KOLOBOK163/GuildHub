<template>
  <header class="header">
    <div class="container header-content">
      <div class="logo">
        <h1>GuildHub</h1>
      </div>
      <nav class="nav-menu">
        <ul>
          <li><router-link to="/">Home</router-link></li>
          <li><router-link to="/teams">Teams</router-link></li>
          <li><router-link to="/players">Players</router-link></li>
          <li><router-link to="/news">News</router-link></li>
          <li><router-link to="/videos">Videos</router-link></li>
          <li><router-link to="/stats">Stats</router-link></li>
        </ul>
      </nav>
      <div class="user-profile">
        <div v-if="isAuthenticated" class="profile-dropdown">
          <div class="profile-trigger" @click="toggleProfile">
            <img v-if="user.avatarUrl" :src="user.avatarUrl" :alt="user.username" class="profile-avatar" />
            <div v-else class="profile-avatar-placeholder">{{ userInitials }}</div>
            <span class="profile-username">{{ user.username }}</span>
            <svg class="dropdown-arrow" :class="{ 'open': showProfileMenu }" width="12" height="12" viewBox="0 0 12 12" fill="none">
              <path d="M2 4L6 8L10 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div v-if="showProfileMenu" class="profile-menu">
            <div class="profile-info">
              <div class="profile-info-item">
                <span class="label">Email:</span>
                <span class="value">{{ user.email || 'N/A' }}</span>
              </div>
              <div class="profile-info-item">
                <span class="label">Username:</span>
                <span class="value">{{ user.username }}</span>
              </div>
            </div>
            <div class="profile-menu-divider"></div>
            <router-link v-if="isAdmin" to="/admin" class="profile-menu-item admin-link">Админ панель</router-link>
            <button class="profile-menu-item" @click="logout">Logout</button>
          </div>
        </div>
        <div v-else class="auth-buttons">
          <router-link to="/login" class="btn-login">Login</router-link>
          <router-link to="/register" class="btn-register">Sign Up</router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      showProfileMenu: false,
      user: {
        username: '',
        email: '',
        avatarUrl: null,
        userId: null
      }
    };
  },
  computed: {
    isAuthenticated() {
      return !!localStorage.getItem('token');
    },
    isAdmin() {
      const roles = JSON.parse(localStorage.getItem('roles') || '[]');
      return roles.includes('ADMIN');
    },
    userInitials() {
      if (!this.user.username) return '?';
      return this.user.username.charAt(0).toUpperCase();
    }
  },
  mounted() {
    this.loadUserData();
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    async loadUserData() {
      const token = localStorage.getItem('token');
      if (!token) return;

      this.user.username = localStorage.getItem('username') || '';
      this.user.email = localStorage.getItem('email') || '';

      try {
        const userId = this.getUserIdFromToken(token);
        if (userId) {
          this.user.userId = userId;
          await this.fetchUserDetails(userId);
        }
      } catch (error) {
        console.error('Error parsing token:', error);
      }
    },
    getUserIdFromToken(token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.user_id || null;
      } catch (error) {
        return null;
      }
    },
    async fetchUserDetails(userId) {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          const userData = await response.json();
          this.user.avatarUrl = userData.avatarUrl;
          if (!this.user.username) this.user.username = userData.username;
          if (!this.user.email) this.user.email = userData.email;
        }
      } catch (error) {
        console.error('Error fetching user details:', error);
      }
    },
    toggleProfile() {
      this.showProfileMenu = !this.showProfileMenu;
    },
    handleClickOutside(event) {
      const profileElement = this.$el.querySelector('.user-profile');
      if (profileElement && !profileElement.contains(event.target)) {
        this.showProfileMenu = false;
      }
    },
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      localStorage.removeItem('roles');
      this.user = { username: '', email: '', avatarUrl: null, userId: null };
      this.showProfileMenu = false;
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.header {
  background-color: #0a0a0a;
  padding: 1rem 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo h1 {
  font-size: 1.8rem;
  font-weight: 700;
  letter-spacing: 1px;
  color: #fff;
  text-shadow: 0 0 5px rgba(255, 255, 255, 0.3);
}

.nav-menu ul {
  list-style: none;
  display: flex;
  gap: 1.5rem;
  margin: 0;
  padding: 0;
}

.nav-menu a {
  color: #ccc;
  text-decoration: none;
  font-size: 0.95rem;
  font-weight: 500;
  padding: 0.5rem 0.75rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  color: #66d9ff;
  background-color: rgba(102, 217, 255, 0.1);
}

.user-profile {
  position: relative;
  display: flex;
  align-items: center;
}

.profile-dropdown {
  position: relative;
}

.profile-trigger {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.profile-trigger:hover {
  background-color: rgba(102, 217, 255, 0.1);
}

.profile-avatar,
.profile-avatar-placeholder {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(102, 217, 255, 0.3);
}

.profile-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  font-weight: 600;
  font-size: 1.1rem;
}

.profile-username {
  color: #ccc;
  font-weight: 500;
  font-size: 0.95rem;
}

.dropdown-arrow {
  color: #ccc;
  transition: transform 0.2s ease;
}

.dropdown-arrow.open {
  transform: rotate(180deg);
}

.profile-menu {
  position: absolute;
  top: calc(100% + 0.5rem);
  right: 0;
  background: rgba(20, 20, 20, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  min-width: 250px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  z-index: 1000;
  overflow: hidden;
}

.profile-info {
  padding: 1rem;
}

.profile-info-item {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  font-size: 0.9rem;
}

.profile-info-item .label {
  color: #aaa;
  font-weight: 500;
}

.profile-info-item .value {
  color: #fff;
  font-weight: 400;
}

.profile-menu-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.1);
  margin: 0.5rem 0;
}

.profile-menu-item {
  width: 100%;
  padding: 0.75rem 1rem;
  background: transparent;
  border: none;
  color: #ff6b6b;
  text-align: left;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
  transition: background-color 0.2s ease;
  font-family: 'Roboto', sans-serif;
}

.profile-menu-item:hover {
  background-color: rgba(255, 107, 107, 0.1);
}

.profile-menu-item.admin-link {
  color: #66d9ff;
  text-decoration: none;
  display: block;
}

.profile-menu-item.admin-link:hover {
  background-color: rgba(102, 217, 255, 0.1);
  color: #66d9ff;
}

.auth-buttons {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.btn-login,
.btn-register {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  text-decoration: none;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.2s ease;
  font-family: 'Roboto', sans-serif;
}

.btn-login {
  color: #ccc;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn-login:hover {
  color: #66d9ff;
  border-color: rgba(102, 217, 255, 0.5);
  background-color: rgba(102, 217, 255, 0.1);
}

.btn-register {
  background: linear-gradient(90deg, #6e8efb, #8a76f0);
  color: white;
  border: none;
}

.btn-register:hover {
  background: linear-gradient(90deg, #8a76f0, #6e8efb);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(110, 142, 251, 0.3);
}
</style>