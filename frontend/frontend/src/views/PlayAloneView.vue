<script setup>
import { ref, onMounted } from 'vue';
import { questionsApi } from '@/api/axios';
import BackButton from "@/components/BackButton.vue";

const question = ref(null);
const selectedAnswer = ref(null);
const correctAnswer = ref(null);

const loadQuestion = async () => {
  try {
    const res = await questionsApi.getRandomQuestion();
    question.value = res.data;
    selectedAnswer.value = null;
    correctAnswer.value = res.data.correctAnswerIndex;
  } catch (err) {
    console.error('Fehler beim Laden der Frage:', err);
  }
};

const selectAnswer = (index) => {
  selectedAnswer.value = index;

  setTimeout(() => {
    loadQuestion();
  }, 3000);
};

const getAnswerClass = (index) => {
  if (selectedAnswer.value === null) return '';
  if (index === correctAnswer.value) return 'correct';
  if (index === selectedAnswer.value) return 'wrong';
  return 'inactive';
};

onMounted(loadQuestion);
</script>

<template>
  <div class="play-alone-container">
    <back-button />
    <h1 class="title">Alleine spielen 🧠</h1>

    <div v-if="question" class="question-box">
      <h2>{{ question.questionText }}</h2>

      <div class="answers">
        <button
            v-for="(answer, index) in question.answers"
            :key="index"
            :class="getAnswerClass(index)"
            @click="selectAnswer(index)"
            :disabled="selectedAnswer !== null"
        >
          {{ answer }}
        </button>
      </div>
    </div>

    <p v-else>Lade nächste Frage...</p>
  </div>
</template>

<style scoped>
/* Styles bleiben unverändert */
.play-alone-container {
  max-width: 600px;
  margin: auto;
  padding: 2rem;
  text-align: center;
}

.title {
  font-size: 2rem;
  margin-bottom: 2rem;
}

.question-box h2 {
  margin-bottom: 1.5rem;
}

.answers {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.answers button {
  padding: 0.8rem;
  border-radius: 12px;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: 0.3s;
}

.correct {
  background-color: #a3e635; /* grün */
  color: #1a1a1a;
}

.wrong {
  background-color: #ef4444; /* rot */
  color: #fff;
}

.inactive {
  background-color: #e5e7eb; /* grau */
  color: #555;
}
</style>