<script setup>
defineProps({
  leaveRequests: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <section class="leave-calendar-view">
    <h2>Calendar-style View</h2>

    <p v-if="leaveRequests.length === 0">No leave requests to show.</p>

    <div v-else class="leave-calendar-view__grid">
      <article
        v-for="request in leaveRequests"
        :key="request.id"
        class="leave-calendar-view__card"
        :class="`leave-calendar-view__card--${request.status.toLowerCase()}`"
      >
        <div class="leave-calendar-view__header">
          <strong>{{ request.teamMember.name }}</strong>

          <span
            class="status-badge"
            :class="`status-badge--${request.status.toLowerCase()}`"
          >
            {{ request.status }}
          </span>
        </div>

        <p class="leave-calendar-view__dates">
          {{ request.startDate }} → {{ request.endDate }}
        </p>

        <p class="leave-calendar-view__reason">
          {{ request.reason }}
        </p>

        <p v-if="request.comments" class="leave-calendar-view__comments">
          {{ request.comments }}
        </p>
      </article>
    </div>
  </section>
</template>
