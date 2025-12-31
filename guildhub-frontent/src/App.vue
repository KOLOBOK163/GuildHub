<template>
  <div id="app">
    <Header v-if="showHeader" />
    <router-view />
  </div>
</template>

<script>
import Header from '@/components/header/Header.vue';

export default {
  name: 'App',
  components: {
    Header
  },
  computed: {
    showHeader() {
      const noHeaderRoutes = ['/login', '/register'];
      return !noHeaderRoutes.includes(this.$route.path);
    }
  }
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Roboto', sans-serif;
  background-color: #0f0f0f;
  color: white;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>