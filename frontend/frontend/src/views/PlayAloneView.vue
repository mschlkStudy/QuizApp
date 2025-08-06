<template>
  <div class="play-alone-container">
    <!-- Top-right controls: Score + Home -->
    <div class="top-right-controls">
      <router-link to="/overview" class="home-button" title="Zur Übersicht">
        <!-- Haus-Icon -->
        <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 12L2 13m10-11L12 2m10 11l1-1m-1 1l-9-9-9 9M5 10v10a1 1 0 001 1h3m10-11v10a1 1 0 01-1 1h-3m-6 0h6" />
        </svg>
      </router-link>

      <div v-if="questionsLoaded" class="score-box">
        <span><strong>Punkte:</strong> {{ score }} / {{ questions.length }}</span>
      </div>
    </div>

    <h1>Alleine spielen</h1>

    <!-- Auswahl Studienfach / Modul -->
    <div v-if="!questionsLoaded" class="form-section">
      <div class="form-group">
        <label for="studySubject">Studienfach</label>
        <select
            id="studySubject"
            v-model="selectedStudySubject"
            required
            @change="loadModules"
        >
          <option value="">Bitte wählen</option>
          <option
              v-for="subject in studySubjects"
              :key="subject.id"
              :value="subject.id"
          >
            {{ subject.name }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="subjectModul">Modul</label>
        <select
            id="subjectModul"
            v-model="selectedModule"
            required
            :disabled="!selectedStudySubject"
        >
          <option value="">Bitte wählen</option>
          <option
              v-for="modul in subjectModuls"
              :key="modul.id"
              :value="modul.id"
          >
            {{ modul.name }}
          </option>
        </select>
      </div>

      <button
          class="start-button"
          @click="startGame"
          :disabled="!selectedModule"
      >
        Spiel starten
      </button>
    </div>

    <!-- Fragen anzeigen -->
    <div v-else-if="currentQuestionIndex < questions.length" class="form-section">
      <h2>Frage {{ currentQuestionIndex + 1 }} von {{ questions.length }}</h2>
      <p class="question-text">{{ currentQuestion.questionText }}</p>

      <div class="answer-list">
        <button
            v-for="(answer, index) in currentQuestion.answers"
            :key="index"
            @click="answerQuestion(index)"
            class="answer-button"
        >
          {{ answer }}
        </button>
      </div>
    </div>

    <!-- Ergebnis -->
    <div v-else class="form-section">
      <h2>Spiel beendet!</h2>
      <p>Du hast <strong>{{ score }}</strong> von {{ questions.length }} Fragen richtig beantwortet.</p>
      <button @click="resetGame" class="start-button">Nochmal spielen</button>
      <button @click="goToOverview" class="start-button">Zurück zur Übersicht</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '@/api/axios.js'
import router from "@/router/index.js";
import { onBeforeRouteLeave } from 'vue-router'

const sessionId = ref(null)
const session = ref(null)
const studySubjects = ref([])
const subjectModuls = ref([])
const route = useRoute();
const selectedStudySubject = ref('')
const selectedModule = ref('')
const questions = ref([])
const currentQuestionIndex = ref(0)
const score = ref(0)
const questionsLoaded = ref(false)
const gameSessionId = ref('')

const currentQuestion = computed(() => questions.value[currentQuestionIndex.value])

const goToOverview = () => {
  router.push('/overview')
}

const loadSubjects = async () => {
  try {
    const response = await api.get('/dropdowns/study-subjects')
    studySubjects.value = response.data
  } catch (error) {
    console.error('Fehler beim Laden der Studienfächer:', error)
  }
}

const loadModules = async () => {
  if (!selectedStudySubject.value) {
    studySubjects.value = []
    selectedStudySubject.value = ''
    return
  }

  try {
    const response = await api.get(`/dropdowns/subject-modules/${selectedStudySubject.value}`)
    subjectModuls.value = response.data
    selectedModule.value = ''
  } catch (error) {
    console.error('Fehler beim Laden der Module:', error)
  }
}

const startGame = async () => {
  try {
    const response = await api.post(
        '/gamesessions/start',
        {}, // kein Body
        {
          params: {
            subjectId: selectedStudySubject.value,
            modulId: selectedModule.value,
            amount: 10
          },
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem('jwt') || ''
          }
        }
    )

    const gameSession = response.data
    questions.value = gameSession.questions
    gameSessionId.value = gameSession.id
    questionsLoaded.value = true
    currentQuestionIndex.value = 0
    score.value = 0
  } catch (error) {
    console.error('Fehler beim Starten des Spiels:', error)
    alert('Spiel konnte nicht gestartet werden. Bitte versuche es erneut.')
  }
}


const answerQuestion =  async (selectedIndex) => {
  const correct = questions.value[currentQuestionIndex.value].correctAnswerIndex
  if (selectedIndex === correct) {
    score.value++
  }
  currentQuestionIndex.value++

  if (currentQuestionIndex.value === questions.value.length) {
    try {
      await api.post(`/gamesessions/${gameSessionId.value}/complete`, {}, {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('jwt')
        }
      })
    } catch (error) {
      console.error('Fehler beim Abschließen der Session:', error)
    }
  } else {
    // Zwischenspeichern des aktuellen Index (optional)
    await updateCurrentIndexAndScore();
  }
}

onBeforeRouteLeave((to, from, next) => {
  if (questionsLoaded.value && gameSessionId.value) {
    updateCurrentIndexAndScore().finally(() => next())
  } else {
    next()
  }
})

const updateCurrentIndexAndScore = async () => {
  try {
    await api.post(`/gamesessions/${gameSessionId.value}/update-indexAndScore`, {
      index: currentQuestionIndex.value,
      score: score.value
    }, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    })
  } catch (error) {
    console.error('Fehler beim Aktualisieren des Fortschritts:', error)
  }
}


const resetGame = () => {
  questionsLoaded.value = false
  selectedStudySubject.value = ''
  selectedModule.value = ''
  questions.value = []
}

const loadSession = async () => {
  try {
    const response = await api.get(`/gamesessions/${sessionId.value}`, {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('jwt')
      }
    })
    session.value = response.data;
    questions.value = session.value.questions;
    currentQuestionIndex.value = parseInt(route.query.startIndex) || session.value.currentQuestionIndex || 0;
    score.value = session.value.score || 0;
    questionsLoaded.value = true;
    gameSessionId.value = session.value.id;
  } catch (error) {
    console.error('Fehler beim Laden der Session:', error)
  }
}

onMounted(() => {
  if (route.query.sessionId) {
    sessionId.value = route.query.sessionId
    loadSession()
  } else {
    loadSubjects()
  }
})


</script>

<style scoped>
.play-alone-container {
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

h2 {
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 1rem;
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

select,
button,
textarea {
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

.question-text {
  font-size: 1.1rem;
  font-weight: 500;
  margin-top: 0.5rem;
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
