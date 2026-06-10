<script setup>
import { onMounted, ref } from "vue";
import api from "./api/api";
import TeamMembers from "./components/TeamMembers.vue";
import LeaveRequestForm from "./components/LeaveRequestForm.vue";

const teamMembers = ref([]);
const leaveRequests = ref([]);
const onCallSchedule = ref([]);
const errorMessage = ref("");

async function loadData() {
  try {
    const [teamMembersResponse, leaveRequestsResponse, onCallResponse] =
      await Promise.all([
        api.get("/team-members"),
        api.get("/leave-requests"),
        api.get("/on-call?weeks=8"),
      ]);

    teamMembers.value = teamMembersResponse.data;
    leaveRequests.value = leaveRequestsResponse.data;
    onCallSchedule.value = onCallResponse.data;
  } catch (error) {
    errorMessage.value = "Failed to load data.";
    console.error(error);
  }
}

onMounted(loadData);
</script>

<template>
  <main class="app">
    <h1>Team Leave Calendar</h1>

    <p v-if="errorMessage" class="error">
      {{ errorMessage }}
    </p>

    <section>
      <TeamMembers :teamMembers="teamMembers" />
    </section>

    <section>
      <LeaveRequestForm :team-members="teamMembers" @created="loadData" />
    </section>

    <section>
      <h2>On-call Schedule</h2>

      <ul>
        <li
          v-for="week in onCallSchedule"
          :key="week.weekStart"
          :class="{ conflict: week.hasConflict }"
        >
          {{ week.weekStart }} to {{ week.weekEnd }}
          —
          {{ week.onCallPerson.name }}
          <strong v-if="week.hasConflict">
            Conflict! Suggested replacement:
            {{ week.suggestedReplacement?.name || "No replacement available" }}
          </strong>
        </li>
      </ul>
    </section>
  </main>
</template>
