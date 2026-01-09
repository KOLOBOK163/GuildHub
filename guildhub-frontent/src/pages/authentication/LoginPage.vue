<template>
  <div class="container">
    <div class="header">
      <h1 class="logo-title">GuildHub</h1>
    </div>

    <div class="login">
      <div class="login-title">
        <h2>Welcome Back!</h2>
        <p>Enter your credentials to continue.</p>
        <div class="login-title_switch">
          Don't have an account? <router-link to="/register">Sign up</router-link>
        </div>
      </div>

      <div class="login-form">
        <form @submit.prevent="login">
          <input type="text" placeholder="Username" v-model="username" required />
          <input type="password" placeholder="Password" v-model="password" required />
          <button type="submit">Log In</button>
        </form>

        <div class="social-login">
          <button class="social-btn discord">Discord</button>
          <button class="social-btn steam">Steam</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async login() {
      try{
        const response = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: this.username,
            password: this.password
          })
        });
        const data = await response.json();
        if (response.ok)
        {
          localStorage.setItem('token', data.accessToken || data.token);
          if (data.username) localStorage.setItem('username', data.username);
          if (data.email) localStorage.setItem('email', data.email);
          if (data.roles) localStorage.setItem('roles', JSON.stringify(data.roles));
          this.$router.push('/');
        }else{
          console.log('Login failed', err);
        }
      
      }catch(err){
        console.log('Login Error:', err);
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  overflow-x: hidden;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100vw;
  font-family: 'Orbitron', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(135deg, #6e8efb, #a777e3); /* Ваш градиент */
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.header {
  position: absolute;
  top: 30px;
  z-index: 10;
  text-align: center;
}

.logo-title {
  font-size: 2.8rem;
  font-weight: 700;
  color: white;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  letter-spacing: 2px;
}


/* Форма входа */
.login {
  width: 100%;
  max-width: 420px;
  padding: 32px;
  border: 2px solid rgba(255, 255, 255, 0.25);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  z-index: 5;
  position: relative;
  overflow: hidden;
}

.login::before,
.login::after {
  content: '';
  position: absolute;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #6e8efb, #a777e3);
}

.login::before {
  top: 0;
}

.login::after {
  bottom: 0;
}

.login-title h2 {
  font-size: 2.1rem;
  color: #333;
  margin-bottom: 8px;
  text-align: center;
  font-weight: 600;
}

.login-title p {
  font-size: 0.95rem;
  color: #666;
  text-align: center;
  margin-bottom: 20px;
}

.login-title_switch {
  font-size: 0.9rem;
  color: #777;
  margin-bottom: 24px;
  text-align: center;
}

.login-title_switch a {
  color: #a777e3;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.login-title_switch a:hover {
  color: #6e8efb;
  text-decoration: underline;
}

/* Поля формы */
.login-form form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.login-form input {
  padding: 14px 16px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  background: white;
  color: #333;
  outline: none;
  transition: all 0.25s ease;
  font-family: 'Orbitron', sans-serif;
}

.login-form input::placeholder {
  color: #aaa;
}

.login-form input:focus {
  border-color: #6e8efb;
  box-shadow: 0 0 0 3px rgba(110, 142, 251, 0.2);
}

.login-form button {
  padding: 14px;
  background: linear-gradient(90deg, #6e8efb, #8a76f0);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1.05rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 600;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 1px;
}

.login-form button:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 6px 15px rgba(110, 142, 251, 0.4);
}

.login-form button:active {
  transform: translateY(0) scale(1);
}

.social-login {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.social-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  font-family: 'Orbitron', sans-serif;
  color: white;
  transition: transform 0.2s, opacity 0.2s;
}

.discord {
  background: #5865f2;
}

.steam {
  background: #1b2838;
}

.social-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.container::before {
  content: '';
  position: absolute;
  left: -5%;
  top: 50%;
  transform: translateY(-50%);
  width: 25%;
  height: 100vh;
  background: url('/t_cs.png') no-repeat center;
  background-size: cover;
  opacity: 0.7;
  pointer-events: none;
  z-index: 0;
}

.container::after {
  content: '';
  position: absolute;
  right: -10%;
  top: 53%;
  transform: translateY(-50%);
  width: 25%;
  height: 90vh;
  background: url('/ct_cs.png') no-repeat center;
  background-size: cover;
  opacity: 0.7;
  pointer-events: none;
  z-index: 0;
}

</style>
