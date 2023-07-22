<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <header>
    <!-- <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" /> -->

    <div class="wrapper">
      <HelloWorld msg="LuSHEIN" />

      <!-- <nav>
        <RouterLink to="/">Home</RouterLink>
      </nav> -->
    </div>
  </header>

  <div class="content-wrapper">
    <RouterView />
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
  display: flex;
  place-items: center;
  padding-right: calc(var(--section-gap) / 2);
}

.wrapper {
  display: flex;
  width: 100%;
  flex-wrap: wrap;
}

.content-wrapper {
  width: 100%;
  padding-top: 2rem; /* Adjust the padding as needed */
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-left: -1rem;
  font-size: 1rem;
  padding: 1rem 0;
  margin-top: 1rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}
</style>
