<template>
  <div class="login-container">
    <h1 class="title">Die interaktive Quizapp der IU</h1>

    <form @submit.prevent="handleLogin" class="login-form">
      <input
          v-model="username"
          type="text"
          placeholder="Benutzername"
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
      <button type="submit" class="button login-button">Login</button>
      <button type="button" class="button register-button" @click="$router.push('/register')">Registrieren</button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/axios'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')

async function handleLogin() {
  try {
    const response = await login(username.value, password.value)
    localStorage.setItem("jwt", response.data.token)
    console.log("Login erfolgreich:", response.data)
    router.push("/overview")
  } catch (err) {
    error.value = "Login fehlgeschlagen. Bitte überprüfe deine Eingaben."
    console.error(err)
  }
}
</script>

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