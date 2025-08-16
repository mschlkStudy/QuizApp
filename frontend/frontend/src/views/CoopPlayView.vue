<template>
  <div class="coop-play-container">
    <!-- Top-right controls -->
    <div class="top-right-controls">
      <router-link to="/overview" class="home-button" title="Zur Übersicht">
        <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 12L2 13m10-11L12 2m10 11l1-1m-1 1l-9-9-9 9M5 10v10a1 1 0 001 1h3m10-11v10a1 1 0 01-1 1h-3m-6 0h6" />
        </svg>
      </router-link>

      <div v-if="questionsLoaded" class="score-box">
        <span><strong>Deine Punkte:</strong> {{ score }}</span>
      </div>
    </div>

    <h1>Gemeinsam spielen</h1>

    <!-- Auswahl Studienfach / Modul + Mitspieler -->
    <div v-if="!questionsLoaded" class="form-section">
      <div class="form-group">
        <label for="studySubject">Studienfach</label>
        <select id="studySubject" v-model="selectedStudySubject" @change="loadModules" required>
          <option value="">Bitte wählen</option>
          <option v-for="subject in studySubjects" :key="subject.id" :value="subject.id">
            {{ subject.name }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="subjectModule">Modul</label>
        <select id="subjectModule" v-model="selectedModule" :disabled="!selectedStudySubject" required>
          <option value="">Bitte wählen</option>
          <option v-for="modul in subjectModules" :key="modul.id" :value="modul.id">
            {{ modul.name }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="inviteUser">Mitspieler einladen (Username)</label>
        <input id="inviteUser" v-model="invitedUser" placeholder="z.B. maxmustermann" />
      </div>

      <button class="start-button" @click="startCoopGame" :disabled="!selectedModule || !invitedUser">
        Coop-Spiel starten
      </button>
    </div>

    <!-- Fragen -->
    <div v-else-if="currentQuestionIndex < questions.length" class="form-section">
      <h2>Frage {{ currentQuestionIndex + 1 }} von {{ questions.length }}</h2>
      <p class="question-text">{{ currentQuestion.questionText }}</p>

      <div class="answer-list">
        <button v-for="(answer, index) in currentQuestion.answers" :key="index" @click="answerQuestion(index)" class="answer-button">
          {{ answer }}
        </button>
      </div>
    </div>

    <!-- Ergebnis -->
    <div v-else class="form-section">
      <h2>Spiel beendet!</h2>
      <p>Deine Punkte: <strong>{{ score }}</strong></p>
      <p>Mitspieler: {{ invitedUser }}</p>
      <button @click="resetGame" class="start-button">Nochmal spielen</button>
      <button @click="goToOverview" class="start-button">Zurück zur Übersicht</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, onBeforeRouteLeave } from 'vue-router'
import { api } from '@/api/axios.js'
import router from '@/router/index.js'

const studySubjects = ref([])
const subjectModules = ref([])
const selectedStudySubject = ref('')
const selectedModule = ref('')
const invitedUser = ref('')
const questions = ref([])
const currentQuestionIndex = ref(0)
const score = ref(0)
const questionsLoaded = ref(false)
const coopSessionId = ref(null)

const route = useRoute()
const currentQuestion = computed(() => questions.value[currentQuestionIndex.value])

const goToOverview = () => {
  router.push('/overview')
}

const loadSubjects = async () => {
  try {
    const res = await api.get('/dropdowns/study-subjects')
    studySubjects.value = res.data
  } catch (err) {
    console.error('Fehler beim Laden der Studienfächer', err)
  }
}

const loadModules = async () => {
  try {
    const res = await api.get(`/dropdowns/subject-modules/${selectedStudySubject.value}`)
    subjectModules.value = res.data
    selectedModule.value = ''
  } catch (err) {
    console.error('Fehler beim Laden der Module', err)
  }
}

const startCoopGame = async () => {
  try {
    const res = await api.post('/coop-sessions/start', {}, {
      params: {
        subjectId: selectedStudySubject.value,
        modulId: selectedModule.value,
        invitedUser: invitedUser.value,
        amount: 10
      },
      headers: { Authorization: 'Bearer ' + (localStorage.getItem('jwt') || '') }
    })
    const session = res.data
    coopSessionId.value = session.id
    questions.value = session.questions
    currentQuestionIndex.value = 0
    score.value = 0
    questionsLoaded.value = true
  } catch (err) {
    console.error('Fehler beim Starten des Coop-Spiels', err)
    alert('Spiel konnte nicht gestartet werden.')
  }
}

const answerQuestion = async (index) => {
  const correct = questions.value[currentQuestionIndex.value].correctAnswerIndex
  if (index === correct) score.value++
  currentQuestionIndex.value++

  try {
    await api.post(`/coop-sessions/${coopSessionId.value}/answer`, {
      questionIndex: currentQuestionIndex.value - 1,
      chosenAnswer: index,
      score: score.value
    }, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
  } catch (err) {
    console.error('Fehler beim Senden der Antwort', err)
  }
}

const resetGame = () => {
  questionsLoaded.value = false
  selectedStudySubject.value = ''
  selectedModule.value = ''
  invitedUser.value = ''
  questions.value = []
}

onBeforeRouteLeave((to, from, next) => {
  if (coopSessionId.value) {
    api.post(`/coop-sessions/${coopSessionId.value}/save-progress`, {
      index: currentQuestionIndex.value,
      score: score.value
    }, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    }).finally(() => next())
  } else {
    next()
  }
})

onMounted(() => {
  loadSubjects()
})
</script>

<style scoped>
.coop-play-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  position: relative;
}
.top-right-controls {
  position: absolute;
  top: 1rem;
  right: 1rem;
  display: flex;
  gap: 1rem;
  align-items: center;
}
.home-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.4rem;
  border-radius: 6px;
  transition: background-color 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}
.home-button:hover {
  background-color: #f1f5f9;
}
.icon {
  width: 24px;
  height: 24px;
  stroke: #333;
}
.score-box {
  background-color: #f1f5f9;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: bold;
  color: #333;
}
h1 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 2rem;
}
.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
select, input, button {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  width: 100%;
}
.start-button {
  padding: 1rem 2rem;
  background-color: #0077cc;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  align-self: start;
}
.start-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.answer-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.answer-button {
  text-align: left;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  background-color: white;
  cursor: pointer;
  transition: background 0.2s ease;
}
.answer-button:hover {
  background-color: #f1f5f9;
}
</style>
