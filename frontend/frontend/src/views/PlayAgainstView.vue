<template>
  <div class="play-against-container">
    <div class="top-right-controls">
      <router-link to="/overview" class="home-button" title="Zur Übersicht">
        <!-- Haus-Icon -->
        <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 12L2 13m10-11L12 2m10 11l1-1m-1 1l-9-9-9 9M5 10v10a1 1 0 001 1h3m10-11v10a1 1 0 01-1 1h-3m-6 0h6" />
        </svg>
      </router-link>
      <div v-if="questionsLoaded" class="score-box">
        <span><strong>Deine Punkte:</strong> {{ score }} / {{ questions.length }}</span>
      </div>
    </div>

    <h1>Gegen einen anderen spielen</h1>

    <!-- Auswahl -->
    <div v-if="!questionsLoaded" class="form-section">
      <div class="form-group">
        <label>Studienfach</label>
        <select v-model="selectedStudySubject" @change="loadModules">
          <option value="">Bitte wählen</option>
          <option v-for="s in studySubjects" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>
      </div>

      <div class="form-group">
        <label>Modul</label>
        <select v-model="selectedModule" :disabled="!selectedStudySubject">
          <option value="">Bitte wählen</option>
          <option v-for="m in subjectModuls" :key="m.id" :value="m.id">{{ m.name }}</option>
        </select>
      </div>

      <button class="start-button" @click="startDuel" :disabled="!selectedModule">
        Spiel starten / beitreten
      </button>
    </div>

    <!-- Fragen -->
    <div v-else-if="currentQuestionIndex < questions.length" class="form-section">
      <h2>Frage {{ currentQuestionIndex + 1 }} von {{ questions.length }}</h2>
      <p class="question-text">{{ currentQuestion.questionText }}</p>
      <div class="answer-list">
        <button v-for="(answer, i) in currentQuestion.answers" :key="i" @click="answerQuestion(i)">
          {{ answer }}
        </button>
      </div>
    </div>

    <!-- Ergebnis -->
    <div v-else class="form-section">
      <h2>Spiel beendet!</h2>
      <p>Du hast {{ score }} Punkte.</p>
      <p v-if="opponentScore !== null">Dein Gegner hat {{ opponentScore }} Punkte.</p>
      <button @click="goToOverview" class="start-button">Zurück zur Übersicht</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, onBeforeRouteLeave } from 'vue-router'
import router from '@/router'
import { api } from '@/api/axios'

const duelId = ref(null)
const questions = ref([])
const currentQuestionIndex = ref(0)
const score = ref(0)
const opponentScore = ref(null)
const questionsLoaded = ref(false)
const studySubjects = ref([])
const subjectModuls = ref([])
const selectedStudySubject = ref('')
const selectedModule = ref('')
const route = useRoute()

const currentQuestion = computed(() => questions.value[currentQuestionIndex.value])

const loadSubjects = async () => {
  const res = await api.get('/dropdowns/study-subjects')
  studySubjects.value = res.data
}
const loadModules = async () => {
  if (!selectedStudySubject.value) return
  const res = await api.get(`/dropdowns/subject-modules/${selectedStudySubject.value}`)
  subjectModuls.value = res.data
}

const startDuel = async () => {
  const res = await api.post('/duels/start-or-join', {}, {
    params: {
      subjectId: selectedStudySubject.value,
      modulId: selectedModule.value,
      amount: 10,
      modulName: selectedModule.value,
      subjectName: selectedStudySubject.value,
    },
    headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
  })
  const data = res.data
  duelId.value = data.id
  questions.value = data.questions
  currentQuestionIndex.value = data.currentQuestionIndex || 0
  score.value = data.score || 0
  opponentScore.value = data.opponentScore || null
  questionsLoaded.value = true
}

const answerQuestion = async (index) => {
  if (index === currentQuestion.value.correctAnswerIndex) {
    score.value++
  }
  currentQuestionIndex.value++
  if (currentQuestionIndex.value === questions.value.length) {
    await api.post(`/duels/${duelId.value}/complete`, {}, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
    fetchOpponentScore()
  } else {
    saveProgress()
  }
}

const saveProgress = async () => {
  await api.post(`/duels/${duelId.value}/update-progress`, {
    index: currentQuestionIndex.value,
    score: score.value
  }, {
    headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
  })
}

const fetchOpponentScore = async () => {
  const res = await api.get(`/duels/${duelId.value}`, {
    headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
  })
  opponentScore.value = res.data.opponentScore
}

onBeforeRouteLeave((to, from, next) => {
  if (questionsLoaded.value && duelId.value) {
    saveProgress().finally(() => next())
  } else {
    next()
  }
})


onMounted(async () => {
  const id = route.query.sessionId || route.query.duelId
  if (id) {
    duelId.value = id
    const res = await api.get(`/duels/${id}`, {
      headers: { Authorization: 'Bearer ' + localStorage.getItem('jwt') }
    })
    questions.value = res.data.questions
    currentQuestionIndex.value = res.data.currentQuestionIndex || 0
    score.value = res.data.score || 0
    opponentScore.value = res.data.opponentScore || null
    questionsLoaded.value = true
  } else {
    loadSubjects()
  }
})

const goToOverview = () => router.push('/overview')
</script>

<style scoped>
.play-against-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.top-right-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.home-button .icon {
  width: 32px;
  height: 32px;
  color: #6b21a8;
  transition: transform 0.2s ease;
}

.home-button:hover .icon {
  transform: scale(1.1);
}

.score-box {
  background-color: #f1f5f9;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: bold;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 1.75rem;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.start-button {
  background-color: #6b21a8;
  color: white;
  padding: 0.75rem 1.25rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.start-button:disabled {
  background-color: #c4b5fd;
  cursor: not-allowed;
}

.start-button:hover:not(:disabled) {
  background-color: #581c87;
}

.question-text {
  font-size: 1.2rem;
  font-weight: 500;
}

.answer-list {
  display: grid;
  gap: 1rem;
}

.answer-list button {
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.answer-list button:hover {
  background-color: #f3e8ff;
}

</style>

