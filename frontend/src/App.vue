<script setup>
import { computed, onMounted, ref } from "vue";
import api from "./api/api";
import TeamMembers from "./components/TeamMembers.vue";
import LeaveRequestForm from "./components/LeaveRequestForm.vue";
import LeaveRequestList from "./components/LeaveRequestList.vue";
import OnCallSchedule from "./components/OnCallSchedule.vue";
import FiltersPanel from "./components/FiltersPanel.vue";
import LeaveCalendarView from "./components/LeaveCalendarView.vue";

const teamMembers = ref([]);
const leaveRequests = ref([]);
const onCallSchedule = ref([]);
const errorMessage = ref("");

const selectedTeamMemberId = ref("");
const selectedStatus = ref("");
const activeTab = ref("requests");

const conflictCount = computed(
  () => onCallSchedule.value.filter((week) => week.hasConflict).length,
);

async function loadData() {
  try {
    errorMessage.value = "";

    const params = new URLSearchParams();

    if (selectedTeamMemberId.value) {
      params.append("teamMemberId", selectedTeamMemberId.value);
    }

    if (selectedStatus.value) {
      params.append("status", selectedStatus.value);
    }

    const leaveRequestsUrl = params.toString()
      ? `/leave-requests?${params.toString()}`
      : "/leave-requests";

    const [teamMembersResponse, leaveRequestsResponse, onCallResponse] =
      await Promise.all([
        api.get("/team-members"),
        api.get(leaveRequestsUrl),
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

function clearFilters() {
  selectedTeamMemberId.value = "";
  selectedStatus.value = "";
  loadData();
}

function handleTeamMemberFilterChange(value) {
  selectedTeamMemberId.value = value;
  loadData();
}

function handleStatusFilterChange(value) {
  selectedStatus.value = value;
  loadData();
}

onMounted(loadData);
</script>

<template>
  <main class="app">
    <h1>Team Leave Calendar</h1>

    <p v-if="errorMessage" class="error">
      {{ errorMessage }}
    </p>

    <div class="tabs" role="tablist" aria-label="Main sections">
      <button
        type="button"
        class="tabs__button"
        :class="{ 'tabs__button--active': activeTab === 'requests' }"
        role="tab"
        :aria-selected="activeTab === 'requests'"
        @click="activeTab = 'requests'"
      >
        Requests
      </button>

      <button
        type="button"
        class="tabs__button"
        :class="{ 'tabs__button--active': activeTab === 'on-call' }"
        role="tab"
        :aria-selected="activeTab === 'on-call'"
        @click="activeTab = 'on-call'"
      >
        On-call
      </button>
    </div>

    <div v-if="activeTab === 'requests'" role="tabpanel" aria-label="Requests">
      <section>
        <TeamMembers :team-members="teamMembers" />
      </section>

      <section>
        <LeaveRequestForm :team-members="teamMembers" @created="loadData" />
      </section>

      <section>
        <FiltersPanel
          :team-members="teamMembers"
          :selected-team-member-id="selectedTeamMemberId"
          :selected-status="selectedStatus"
          @update:selected-team-member-id="handleTeamMemberFilterChange"
          @update:selected-status="handleStatusFilterChange"
          @clear="clearFilters"
        />
      </section>

      <section>
        <LeaveRequestList
          :leave-requests="leaveRequests"
          @status-updated="loadData"
        />
      </section>

      <section>
        <LeaveCalendarView :leave-requests="leaveRequests" />
      </section>
    </div>

    <div v-else role="tabpanel" aria-label="On-call">
      <section>
        <p class="conflict-summary" v-if="onCallSchedule.length > 0">
          {{ conflictCount }} conflict{{ conflictCount === 1 ? "" : "s" }} in
          the next {{ onCallSchedule.length }} weeks.
        </p>
        <p class="conflict-summary" v-else>No on-call weeks loaded.</p>

        <OnCallSchedule :on-call-schedule="onCallSchedule" />
      </section>
    </div>
  </main>
</template>
