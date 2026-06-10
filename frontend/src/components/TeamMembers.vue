<script setup>
defineProps({
  teamMembers: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <section class="team-members">
    <h2>Team Members</h2>

    <ul class="team-members__list">
      <li
        v-for="member in teamMembers"
        :key="member.id"
        class="team-members__item"
      >
        {{ member.name }}
      </li>
    </ul>
  </section>
</template>
