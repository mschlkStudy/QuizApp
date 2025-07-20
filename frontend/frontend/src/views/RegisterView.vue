<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/axios'

const router = useRouter()
const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const error = ref('')

async function handleRegister() {
  if (password.value !== confirmPassword.value) {
    error.value = 'Die Passwörter stimmen nicht überein!'
    return
  }

  try {
    await register({
      username: username.value,
      password: password.value,
      email: email.value
    })
    router.push('/login')
  } catch (err) {
    error.value = 'Registrierung fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.'
    console.error(err)
  }
}
</script>

<template>
  <div class="login-container">
    <h1 class="title">Registrierung</h1>

    <form @submit.prevent="handleRegister" class="login-form">
      <input
          v-model="username"
          type="text"
          placeholder="Benutzername"
          class="input"
          required
      />
      <input
          v-model="email"
          type="email"
          placeholder="E-Mail"
          class="input"
          required
      />
      <input
          v-model="password"
          type="password"
          placeholder="Passwort"
          class="input"
          required
      />
      <input
          v-model="confirmPassword"
          type="password"
          placeholder="Passwort bestätigen"
          class="input"
          required
      />
      <button type="submit" class="button register-button">Registrieren</button>
      <button type="button" class="button login-button" @click="router.push('/login')">
        Zurück zum Login
      </button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 80px auto;
  padding: 2rem;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  border-radius: 16px;
  background-color: #f9f9f9;
  text-align: center;
}

.title {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input {
  padding: 0.75rem;
  border-radius: 12px;
  border: 1px solid #ccc;
  font-size: 1rem;
}

.button {
  padding: 0.75rem;
  border: none;
  border-radius: 12px;
  background-color: #0056b3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}

.button:hover {
  background-color: #003d80;
}

.error {
  margin-top: 1rem;
  color: red;
}
</style>