<template>
  <div class="coop-play">
    <BackButton />
    <h1>Kooperatives Spiel</h1>

    <div v-if="question">
      <h2>{{ question.text }}</h2>
      <ul>
        <li
            v-for="(answer, index) in question.answers"
            :key="index"
            :class="getAnswerClass(index)"
            @click="selectAnswer(index)"
        >
          {{ answer }}
        </li>
      </ul>
    </div>

    <div v-else>
      <p>Frage wird geladen...</p>
    </div>

    <div class="team-status">
      <h3>Team-Status</h3>
      <ul>
        <li v-for="(member, idx) in teamStatus" :key="idx">
          {{ member.name }}: {{ member.answered ? "✔" : "…" }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import BackButton from '@/components/BackButton.vue'


export default {
  name: 'CoopPlayView',
  components: { BackButton },
  data() {
    return {
      question: null,
      selectedAnswer: null,
      correctAnswerIndex: null,
      teamStatus: [],
    }
  },
  methods: {
    async loadQuestion() {
      try {
        const res = await axios.get('/gamesession/coop/question')
        this.question = res.data
        this.selectedAnswer = null
        this.correctAnswerIndex = null
      } catch (err) {
        console.error('Fehler beim Laden der Frage:', err)
      }
    },
    async selectAnswer(index) {
      if (this.selectedAnswer !== null) return
      this.selectedAnswer = index

      const res = await axios.post('/gamesession/coop/answer', {
        questionId: this.question.id,
        selectedIndex: index,
      })

      this.correctAnswerIndex = res.data.correctIndex
      this.updateTeamStatus()

      setTimeout(() => {
        this.loadQuestion()
      }, 3000)
    },
    getAnswerClass(index) {
      if (this.selectedAnswer === null) return ''
      if (index === this.correctAnswerIndex) return 'correct'
      if (index === this.selectedAnswer) return 'wrong'
      return 'disabled'
    },
    async updateTeamStatus() {
      const res = await axios.get('/gamesession/coop/team-status')
      this.teamStatus = res.data
    },
  },
  mounted() {
    this.loadQuestion()
    this.updateTeamStatus()
    // Optional: wiederkehrende Team-Status Updates
    setInterval(this.updateTeamStatus, 5000)
  },
}
</script>

<style scoped>
.coop-play {
  padding: 1rem;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  margin: 0.5rem 0;
  padding: 0.75rem;
  background: #eee;
  border-radius: 8px;
  cursor: pointer;
}
.correct {
  background-color: #a0e6a0;
}
.wrong {
  background-color: #f49a9a;
}
.disabled {
  opacity: 0.5;
  pointer-events: none;
}
.team-status {
  margin-top: 2rem;
}
</style>
