<template>
  <div class="overview-container">
    <button class="logout-button" @click="logout" title="Abmelden">🚪</button>
    <h1 class="title">Willkommen in der Hauptübersicht</h1>

    <div class="grid">
      <button class="widget" @click="goToPlayAlone">🎮 Alleine spielen</button>
      <button class="widget" @click="goToCoopPlay">🤝 Im Team spielen</button>
      <button class="widget">⚔️ Gegen Kommilitonen spielen</button>
      <button class="widget" @click="goToCreateQuestion">✍️ Frage einreichen</button>
    </div>

    <!-- Neue Sektion: GameSessions -->
    <div class="gamesessions-section">
      <h2>🕒 Offene Spielsessions</h2>
      <p v-if="openSessions.length === 0">Keine offenen Sessions</p>
      <ul>
        <li v-for="session in openSessions" :key="session.id" class="session-item">
          {{ session.studySubjectName }} – {{ session.modulName }} <br />
          Gestartet am: {{ formatDate(session.startedAt) }}
          <button @click="resumeSession(session.id)">Fortsetzen</button>
        </li>
      </ul>

      <h2 class="mt-6">✅ Abgeschlossene Spielsessions</h2>
      <p v-if="completedSessions.length === 0">Noch keine abgeschlossen</p>
      <ul>
        <li v-for="session in completedSessions" :key="session.id" class="session-item">
          {{ session.studySubjectName }} – {{ session.modulName }} <br />
          Gestartet am: {{ formatDate(session.startedAt) }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import BackButton from '@/components/BackButton.vue'
import { api } from '@/api/axios'

const router = useRouter()

function logout() {
  localStorage.removeItem('jwt');
  router.push({ name: 'Login' });
}

function goToCreateQuestion() {
  router.push({ name: 'CreateQuestion' })
}
function goToPlayAlone() {
  router.push({ name: 'PlayAlone' })
}
function goToCoopPlay() {
  router.push({ name: 'CoopPlay' })
}

// Sessions laden
const openSessions = ref([])
const completedSessions = ref([])

const loadSessions = async () => {
  try {
    const response = await api.get('/gamesessions/open', {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    })
    const allSessions = response.data

    openSessions.value = allSessions.filter(s => !s.completed)
    completedSessions.value = allSessions.filter(s => s.completed)
  } catch (error) {
    console.error('Fehler beim Laden der Sessions:', error)
  }
}

// Weiter an Spiel weiterleiten
const resumeSession = (sessionId) => {
  router.push({ name: 'PlayAlone', query: { sessionId } })
}

const formatDate = (isoDate) => {
  const date = new Date(isoDate)
  return date.toLocaleString('de-DE', {
    dateStyle: 'short',
    timeStyle: 'short'
  })
}

onMounted(loadSessions)
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
  margin-bottom: 3rem;
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

.gamesessions-section {
  width: 100%;
  max-width: 600px;
  text-align: left;
}

.gamesessions-section h2 {
  font-size: 1.2rem;
  margin: 1rem 0 0.5rem;
  color: #444;
}

.session-item {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  border: 1px solid #ddd;
}

.session-item button {
  margin-top: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #0077cc;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
</style>
