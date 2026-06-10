<script setup>
import api from "../api/api";

const props = defineProps({
  leaveRequests: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["status-updated"]);

async function updateStatus(requestId, status) {
  await api.patch(`/leave-requests/${requestId}/status`, {
    status,
  });

  emit("status-updated");
}
</script>

<template>
  <section class="leave-request-list">
    <h2>Leave Requests</h2>

    <p v-if="leaveRequests.length === 0">No leave requests yet.</p>

    <div v-else class="leave-request-list__table-wrapper">
      <table class="leave-request-list__table">
        <thead>
          <tr>
            <th>Team Member</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Reason</th>
            <th>Comments</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="request in leaveRequests" :key="request.id">
            <td>{{ request.teamMember.name }}</td>
            <td>{{ request.startDate }}</td>
            <td>{{ request.endDate }}</td>
            <td>{{ request.reason }}</td>
            <td>{{ request.comments || "-" }}</td>
            <td>
              <span
                class="status-badge"
                :class="`status-badge--${request.status.toLowerCase()}`"
              >
                {{ request.status }}
              </span>
            </td>
            <td>
              <div class="leave-request-list__actions">
                <button
                  type="button"
                  :disabled="request.status !== 'PENDING'"
                  @click="updateStatus(request.id, 'APPROVED')"
                >
                  Approve
                </button>

                <button
                  type="button"
                  :disabled="request.status !== 'PENDING'"
                  @click="updateStatus(request.id, 'REJECTED')"
                >
                  Reject
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>
