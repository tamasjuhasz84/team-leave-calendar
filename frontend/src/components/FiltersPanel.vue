<script setup>
defineProps({
  teamMembers: {
    type: Array,
    required: true,
  },
  selectedTeamMemberId: {
    type: String,
    required: true,
  },
  selectedStatus: {
    type: String,
    required: true,
  },
});

const emit = defineEmits([
  "update:selectedTeamMemberId",
  "update:selectedStatus",
  "clear",
]);
</script>

<template>
  <section class="filters-panel">
    <h2>Filters</h2>

    <div class="filters-panel__controls">
      <div class="filters-panel__field">
        <label>Team Member</label>

        <select
          :value="selectedTeamMemberId"
          @change="emit('update:selectedTeamMemberId', $event.target.value)"
        >
          <option value="">All team members</option>

          <option
            v-for="member in teamMembers"
            :key="member.id"
            :value="member.id"
          >
            {{ member.name }}
          </option>
        </select>
      </div>

      <div class="filters-panel__field">
        <label>Status</label>

        <select
          :value="selectedStatus"
          @change="emit('update:selectedStatus', $event.target.value)"
        >
          <option value="">All statuses</option>
          <option value="PENDING">Pending</option>
          <option value="APPROVED">Approved</option>
          <option value="REJECTED">Rejected</option>
        </select>
      </div>

      <button
        type="button"
        class="filters-panel__clear-button"
        @click="emit('clear')"
      >
        Clear filters
      </button>
    </div>
  </section>
</template>
