<script setup>
import { ref } from "vue";
import api from "../api/api";

const props = defineProps({
  teamMembers: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["created"]);

const form = ref({
  teamMemberId: "",
  startDate: "",
  endDate: "",
  reason: "",
  comments: "",
});

const successMessage = ref("");
const errorMessage = ref("");

async function submitForm() {
  successMessage.value = "";
  errorMessage.value = "";

  try {
    await api.post("/leave-requests", {
      teamMemberId: Number(form.value.teamMemberId),
      startDate: form.value.startDate,
      endDate: form.value.endDate,
      reason: form.value.reason,
      comments: form.value.comments,
    });

    successMessage.value = "Leave request created successfully.";

    form.value = {
      teamMemberId: "",
      startDate: "",
      endDate: "",
      reason: "",
      comments: "",
    };

    emit("created");
  } catch (error) {
    errorMessage.value =
      error.response?.data?.message || "Failed to create leave request.";
  }
}
</script>

<template>
  <section class="leave-request-form">
    <h2>Create Leave Request</h2>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>
          Team Member
          <span class="required-indicator" aria-hidden="true">*</span>
        </label>

        <select v-model="form.teamMemberId" required>
          <option value="">Select team member</option>

          <option
            v-for="member in teamMembers"
            :key="member.id"
            :value="member.id"
          >
            {{ member.name }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label>
          Start Date
          <span class="required-indicator" aria-hidden="true">*</span>
        </label>

        <input v-model="form.startDate" type="date" required />
      </div>

      <div class="form-group">
        <label>
          End Date <span class="required-indicator" aria-hidden="true">*</span>
        </label>

        <input v-model="form.endDate" type="date" required />

        <p class="form-helper-text">
          End date must be the same or after start date.
        </p>
      </div>

      <div class="form-group">
        <label>
          Reason <span class="required-indicator" aria-hidden="true">*</span>
        </label>

        <input v-model="form.reason" type="text" required />
      </div>

      <div class="form-group">
        <label>Comments</label>

        <textarea v-model="form.comments" rows="4"></textarea>
      </div>

      <button type="submit">Create Request</button>
    </form>

    <p v-if="successMessage" class="success">
      {{ successMessage }}
    </p>

    <p v-if="errorMessage" class="error">
      {{ errorMessage }}
    </p>
  </section>
</template>
