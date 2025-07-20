<template>
  <div class="overview-container">
    <back-button />
    <h1 class="title">Willkommen in der Hauptübersicht</h1>
    <div class="grid">
      <button class="widget" @click="goToPlayAlone">🎮 Alleine spielen</button>
      <button class="widget" @click="goToCoopPlay">🤝 Im Team spielen</button>
      <button class="widget">⚔️ Gegen Kommilitonen spielen</button>
      <button class="widget" @click="goToCreateQuestion">✍️ Frage einreichen</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import BackButton from "@/components/BackButton.vue";
const router = useRouter()

function goToCreateQuestion() {
  router.push({ name: 'CreateQuestion' })
}
function goToPlayAlone() {
  router.push({name: 'PlayAlone'})
}
function goToCoopPlay() {
  router.push({name: 'CoopPlay'})
}
</script>

<style scoped>
.overview-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  font-family: Arial, sans-serif;
}

.title {
  font-size: 2rem;
  margin-bottom: 2rem;
  color: #333;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  width: 100%;
  max-width: 600px;
}

.widget {
  padding: 1.5rem;
  background-color: #0077cc;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.widget:hover {
  background-color: #005fa3;
}
</style>
