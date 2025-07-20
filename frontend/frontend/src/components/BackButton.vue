<!-- src/components/BackButton.vue -->
<template>
  <div class="p-4">
    <button
        @click="goBack"
        class="bg-gray-200 text-gray-800 rounded-xl px-4 py-2 shadow hover:bg-gray-300 transition"
    >
      ⬅️ Zurück
    </button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function goBack() {
  router.back()
}
</script>
