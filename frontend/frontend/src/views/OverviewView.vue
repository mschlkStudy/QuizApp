<template>
  <div class="overview-container">
    <div class="header-row">
      <div class="user-info">Angemeldet als: {{ username }}</div>
      <button class="logout-button" @click="logout" title="Abmelden">🚪</button>
    </div>
    <h1 class="title">Willkommen {{ username }}</h1>

    <div class="grid">
      <button class="widget" @click="goToPlayAlone">🎮 Alleine spielen</button>
      <button class="widget" @click="goToCoopPlay">🤝 Im Team spielen</button>
      <button class="widget" @click="goToPlayAgainst">⚔️ Gegen Kommilitonen spielen</button>
      <button class="widget" @click="goToCreateQuestion">✍️ Frage einreichen</button>
    </div>

    <!-- Neue Sektion: GameSessions -->
    <div class="gamesessions-section">
      <div class="gamesessions-solo">
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
      <div class="gamesessions-duell">
        <h2>🕒 Offene Duellsessions</h2>
        <div class="session-list">
          <p v-if="openDuels.length === 0">Keine offenen Sessions</p>
          <ul>
            <li v-for="session in openDuels" :key="session.id" class="session-item">
              <strong>{{ session.subjectName }} – {{ session.modulName }}</strong> <br />
              Player 1: {{session.player1}} || Player 2: {{session.player2}} <br />
              Status: {{session.status}} <br />
              <button @click="resumeDuellSession(session)">Fortsetzen</button>
            </li>
          </ul>
        </div>
        <h2 class="mt-6">✅ Abgeschlossene Duellsession</h2>
        <div class="session-list">
          <p v-if="completedDuels.length === 0">Noch keine abgeschlossen</p>
          <ul>
            <li v-for="session in completedDuels" :key="session.id" class="session-item">
              <strong>{{ session.subjectName }} – {{ session.modulName }} </strong><br />
              <strong>Ergebnis:</strong>  {{session.player1Score}} / {{session.player2Score}}<br />
              <strong>Sieger:</strong>  {{session.winner}}
            </li>
          </ul>
        </div>
      </div>
      <div class="gamesessions-coop">
        <h2>🕒 Offene Coop-Sessions</h2>
        <div class="session-list">
          <p v-if="openCoopSessions.length === 0">Keine offenen Coop-Sessions</p>
          <ul>
            <li v-for="session in openCoopSessions" :key="session.id" class="session-item">
              <strong>{{ session.subjectName }} – {{ session.modulName }}</strong> <br />
              Spieler: {{ session.players.map(p => p.username).join(', ') }} <br />
              Status: {{ session.status }} <br />
              <button @click="joinSession(session)">Beitreten</button>
            </li>
          </ul>
        </div>

        <h2 class="mt-6">✅ Abgeschlossene Coop-Sessions</h2>
        <div class="session-list">
          <p v-if="completedCoopSessions.length === 0">Noch keine abgeschlossen</p>
          <ul>
            <li v-for="session in completedCoopSessions" :key="session.id" class="session-item">
              <strong>{{ session.subjectName }} – {{ session.modulName }}</strong><br />
              Spieler: {{ session.players.map(p => p.username).join(', ') }} <br />
              Ergebnis: {{ session.score }} / {{ session.questions?.length || '-' }}
            </li>
          </ul>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '@/api/axios'

const router = useRouter()
const openSessions = ref([])
const completedSessions = ref([])
const openDuels = ref([])
const completedDuels = ref([])
const openCoopSessions = ref([])
const completedCoopSessions = ref([])
const username = ref('');

function logout() {
  localStorage.removeItem('jwt');
  router.push({ name: 'Login' });
}

function goToCreateQuestion() { router.push({ name: 'CreateQuestion' }) }
function goToPlayAlone() { router.push({ name: 'PlayAlone' }) }
function goToCoopPlay() { router.push({ name: 'CoopPlay' }) }
function goToPlayAgainst() { router.push({ name: 'PlayAgainst' }) }

function decodeJwt(token) {
  try {
    const payload = token.split('.')[1];
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    const parsed = JSON.parse(json);
    return parsed['username'] || parsed['sub'] || '';
  } catch(err) {
    return '';
  }
}

const loadSessions = async () => {
  try {
    const response = await api.get('/gamesessions/open', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
    openSessions.value = response.data
  } catch (error) {
    console.error('Fehler beim Laden der Sessions:', error)
  }
}

const loadDuelSessions = async () => {
  try {
    const resOpen = await api.get('/duels/overview/open', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    openDuels.value = resOpen.data;
  } catch (error) {
    console.error('Fehler beim Laden der offenen Duellsessions:', error);
  }
};

const loadCompletedDuelSessions = async () => {
  try {
    const resCompleted = await api.get('/duels/overview/completed', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    completedDuels.value = resCompleted.data;
  } catch (error) {
    console.error('Fehler beim Laden der abgeschlossenen Duellsessions:', error);
  }
}

const loadCompletedSessions = async () => {
  try {
    const response = await api.get('/gamesessions/completed', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
    completedSessions.value = response.data
  } catch (error) {
    console.error('Fehler beim Laden der abgeschlossenen Sessions:', error)
  }
}
const loadCoopSessions = async () => {
  try {
    const resOpen = await api.get('/coop-session/overview/open', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    openCoopSessions.value = resOpen.data;
  } catch (error) {
    console.error('Fehler beim Laden der offenen CoopSessions:', error);
  }
};

const loadCompletedCoopSessions = async () => {
  try {
    const resCompleted = await api.get('/coop-session/overview/completed', {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    completedCoopSessions.value = resCompleted.data;
  } catch (error) {
    console.error('Fehler beim Laden der abgeschlossenen CoopSessions:', error);
  }
};


const resumeSession = async (session) => {
  try {
    const response = await api.get(`/gamesessions/${session.id}`, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    const sessionData = response.data;
    router.push({
      name: 'PlayAlone',
      query: {
        sessionId: sessionData.id,
        startIndex: sessionData.currentQuestionIndex || 0
      },
      state: { sessionData }
    });
  } catch (error) {
    console.error('Fehler beim Laden der Session:', error);
    alert('Die Session konnte nicht geladen werden.');
  }
};

const resumeDuellSession = async (session) => {
  try {
    const response = await api.get(`/duels/${session.id}`, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    const sessionData = response.data;
    router.push({
      name: 'PlayAgainst',
      query: {
        sessionId: sessionData.id,
        startIndex: sessionData.currentQuestionIndex || 0
      },
      state: { sessionData }
    });
  } catch (error) {
    console.error('Fehler beim Laden der Session:', error);
    alert('Die Session konnte nicht geladen werden.');
  }
};

const resumeCoopSession = async (session) => {
  try {
    const response = await api.get(`/coop-session/${session.id}/join`, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    });
    const sessionData = response.data;
    router.push({
      name: 'CoopPlay',
      query: {
        sessionId: sessionData.id,
        startIndex: sessionData.currentQuestionIndex || 0
      },
      state: { sessionData }
    });
  } catch (error) {
    console.error('Fehler beim Laden der Coop-Session:', error);
    alert('Die Coop-Session konnte nicht geladen werden.');
  }
};

const joinSession = async (session) => {
  try {
    const res = await api.post(`/coop-session/${session.id}/join`, {}, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
    const session = res.data
    coopSessionId.value = session.id
    questions.value = session.questions
    players.value = session.players
    currentQuestionIndex.value = 0
    score.value = 0
    questionsLoaded.value = true
  } catch (err) {
    console.error('Fehler beim Beitreten zur Session', err)
    alert('Konnte Session nicht beitreten.')
  }
}



const formatDate = (isoDate) => {
  const date = new Date(isoDate)
  return date.toLocaleString('de-DE', {
    dateStyle: 'short',
    timeStyle: 'short'
  })
}

onMounted(() => {
  const jwt = localStorage.getItem('jwt');
  if (jwt) username.value = decodeJwt(jwt);

  loadSessions();
  loadCompletedSessions();
  loadDuelSessions();
  loadCompletedDuelSessions();
  loadCoopSessions();
  loadCompletedCoopSessions();
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

.header-row {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.2rem;
}

.user-info {
  font-size: 1.05rem;
  color: #333;
  font-weight: 600;
  margin-left: 0.2rem;
}

.logout-button {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  margin-right: 0.2rem;
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
  background-color: #b2d0e7;
  color: black;
  border: 1px solid;
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
  width: 90%;
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  margin: 0 auto;
}

.gamesessions-solo,
.gamesessions-duell {
  flex: 1;
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
  background-color:#b2d0e7;
  color: black;
  border: 1px solid;
  border-radius: 8px;
  cursor: pointer;
}

.mt-6 { margin-top: 1.5rem; }
</style>