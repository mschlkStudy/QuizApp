<template>
  <div class="overview-container">
    <button class="logout-button" @click="logout" title="Abmelden">🚪</button>
    <h1 class="title">Willkommen Username noch hinterlegen</h1>

    <div class="grid">
      <button class="widget" @click="goToPlayAlone">🎮 Alleine spielen</button>
      <button class="widget" @click="goToCoopPlay">🤝 Im Team spielen</button>
      <button class="widget">⚔️ Gegen Kommilitonen spielen</button>
      <button class="widget" @click="goToCreateQuestion">✍️ Frage einreichen</button>
    </div>

    <!-- Neue Sektion: GameSessions -->
    <div class="gamesessions-section">
      <h2>🕒 Offene Spielsessions</h2>
      <div class="session-list">
        <p v-if="openSessions.length === 0">Keine offenen Sessions</p>
        <ul>
          <li v-for="session in openSessions" :key="session.id" class="session-item">
            <strong>{{ session.studySubjectName }} – {{ session.modulName }}</strong> <br />
            Gestartet am: {{ formatDate(session.startedAt) }}
            <button @click="resumeSession(session)">Fortsetzen</button>
          </li>
        </ul>
      </div>

      <h2 class="mt-6">✅ Abgeschlossene Spielsessions</h2>
      <div class="session-list">
        <p v-if="completedSessions.length === 0">Noch keine abgeschlossen</p>
        <ul>
          <li v-for="session in completedSessions" :key="session.id" class="session-item">
            <strong>{{ session.studySubjectName }} – {{ session.modulName }} </strong><br />
            Gestartet am: {{ formatDate(session.startedAt) }} <br />
            Punktzahl: <strong> {{session.score}} / {{session.questions?.length || '-'}}</strong>
          </li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import BackButton from '@/components/BackButton.vue'
import { api } from '@/api/axios'

const router = useRouter()
const openSessions = ref([])
const completedSessions = ref([])

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


const loadSessions = async () => {
  try {
    const response = await api.get('/gamesessions/open', {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    })
    openSessions.value = response.data

    // openSessions.value = allSessions.filter(s => !s.completed)
    // completedSessions.value = allSessions.filter(s => s.completed)
  } catch (error) {
    console.error('Fehler beim Laden der Sessions:', error)
  }
}

const loadCompletedSessions = async () => {
  try {
    const response = await api.get('/gamesessions/completed', {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    })
     completedSessions.value = response.data

    // openSessions.value = allSessions.filter(s => !s.completed)
    // completedSessions.value = allSessions.filter(s => s.completed)
  } catch (error) {
    console.error('Fehler beim Laden der abgeschlossenen Sessions:', error)
  }
}

// Weiter an Spiel weiterleiten
const resumeSession = async (session) => {
  try {
    const response = await api.get(`/gamesessions/${session.id}`, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    });

    const sessionData = response.data;

    router.push({
      name: 'PlayAlone',
      query: {
        sessionId: sessionData.id,
        startIndex: sessionData.currentQuestionIndex || 0
      },
      state: {
        sessionData // Optional: wenn du Zustand mitgeben willst (z. B. in Pinia oder über router state)
      }
    });
  } catch (error) {
    console.error('Fehler beim Laden der Session:', error);
    alert('Die Session konnte nicht geladen werden.');
  }
};



const formatDate = (isoDate) => {
  const date = new Date(isoDate)
  return date.toLocaleString('de-DE', {
    dateStyle: 'short',
    timeStyle: 'short'
  })
}

onMounted(() => {
  loadSessions();
  loadCompletedSessions();
})
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

.session-list {
   max-height: 250px;
   overflow-y: auto;
   border: 1px solid #ccc;
   border-radius: 10px;
   padding: 0.5rem 1rem;
   margin-bottom: 1rem;
   background-color: #f9f9f9;
   box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
 }

.session-item {
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px dashed #ddd;
}

.session-item:last-child {
  border-bottom: none;
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
