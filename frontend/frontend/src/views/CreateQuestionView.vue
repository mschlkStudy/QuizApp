<template>
  <div class="create-container">
    <back-button />
    <h1>Frage einreichen</h1>

    <form @submit.prevent="submitQuestion" class="question-form">
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
          v-model="selectedSubjectModul" 
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

      <div class="form-group">
        <label for="questionText">Frage:</label>
        <textarea 
          id="questionText"
          v-model="questionText" 
          required
          rows="3"
          placeholder="Geben Sie hier Ihre Frage ein"
        ></textarea>
      </div>

      <div class="form-group">
        <label>Antwortmöglichkeiten:</label>
        <div 
          v-for="(answer, index) in answers" 
          :key="index" 
          class="answer-option"
        >
          <input 
            v-model="answers[index]" 
            type="text" 
            :placeholder="`Antwort ${index + 1}`" 
            required 
          />
          <div class="correct-answer">
            <input 
              type="radio" 
              name="correctAnswer" 
              :id="`answer${index}`"
              :value="index" 
              v-model="correctAnswerIndex" 
            />
            <label :for="`answer${index}`">Korrekt</label>
          </div>
        </div>
      </div>

      <button type="submit" :disabled="!isFormValid">Absenden</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { api } from '@/api/axios';
import BackButton from "@/components/BackButton.vue";
import { useRouter } from 'vue-router';

const router = useRouter();

// Formulardaten
const questionText = ref('');
const answers = ref(['', '', '', '']);
const correctAnswerIndex = ref(null);
const selectedStudySubject = ref('');
const selectedSubjectModul = ref('');

// Dropdown-Daten
const studySubjects = ref([]);
const subjectModuls = ref([]);

// Formularvalidierung
const isFormValid = computed(() => {
  return questionText.value.trim() !== '' &&
         selectedStudySubject.value !== '' &&
         selectedSubjectModul.value !== '' &&
         correctAnswerIndex.value !== null &&
         answers.value.every(answer => answer.trim() !== '');
});

// Laden der Studienfächer
const loadStudySubjects = async () => {
  try {
    const response = await api.get('/dropdowns/study-subjects');
    studySubjects.value = response.data;
  } catch (error) {
    console.error('Fehler beim Laden der Studienfächer:', error);
    // Hier könnte eine Fehlerbehandlung erfolgen
  }
};

// Laden der Module für das ausgewählte Studienfach
const loadModules = async () => {
  if (!selectedStudySubject.value) {
    subjectModuls.value = [];
    selectedSubjectModul.value = '';
    return;
  }

  try {
    const response = await api.get(`/dropdowns/subject-modules/${selectedStudySubject.value}`);
    subjectModuls.value = response.data;
    selectedSubjectModul.value = ''; // Reset der Modulauswahl
  } catch (error) {
    console.error('Fehler beim Laden der Module:', error);
    // Hier könnte eine Fehlerbehandlung erfolgen
  }
};

// Absenden der Frage
const submitQuestion = async () => {
  if (!isFormValid.value) {
    alert('Bitte füllen Sie alle Felder aus und wählen Sie eine korrekte Antwort.');
    return;
  }

  const payload = {
    questionText: questionText.value,
    answers: answers.value,
    correctAnswerIndex: correctAnswerIndex.value,
    studySubjectId: selectedStudySubject.value,
    modulId: selectedSubjectModul.value
  };

  try {
    await api.post('/questions', payload);
    alert('Frage wurde erfolgreich eingereicht!');
    router.push('/'); // Zurück zur Hauptseite
  } catch (error) {
    console.error('Fehler beim Einreichen der Frage:', error);
    alert('Fehler beim Einreichen der Frage. Bitte versuchen Sie es später erneut.');
  }
};

// Initial laden der Studienfächer
onMounted(loadStudySubjects);
</script>

<style scoped>
.create-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
}

.question-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-weight: bold;
  color: #333;
}

select, input[type="text"], textarea {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  width: 100%;
}

textarea {
  resize: vertical;
  min-height: 100px;
}

.answer-option {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-bottom: 0.5rem;
}

.correct-answer {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

button {
  padding: 1rem 2rem;
  background-color: #0077cc;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}
</style>