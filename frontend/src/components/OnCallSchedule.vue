<script setup>
defineProps({
  onCallSchedule: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <section class="on-call-schedule">
    <h2>On-call Schedule</h2>

    <p v-if="onCallSchedule.length === 0">No on-call schedule available.</p>

    <div v-else class="on-call-schedule__table-wrapper">
      <table class="on-call-schedule__table">
        <thead>
          <tr>
            <th>Week</th>
            <th>On-call Person</th>
            <th>Conflict</th>
            <th>Suggested Replacement</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="week in onCallSchedule"
            :key="week.weekStart"
            :class="{ 'on-call-schedule__row--conflict': week.hasConflict }"
          >
            <td>{{ week.weekStart }} to {{ week.weekEnd }}</td>

            <td>
              {{ week.onCallPerson.name }}
            </td>

            <td>
              <span v-if="week.hasConflict" class="conflict-badge">
                Conflict
              </span>

              <span v-else class="no-conflict-badge"> No conflict </span>
            </td>

            <td>
              <span v-if="week.suggestedReplacement">
                {{ week.suggestedReplacement.name }}
              </span>

              <span v-else> - </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>
