<template>
  <div class="home">
    <EventListPage/>
  </div>
</template>

<script>
// @ is an alias to /src
import EventListPage from '@/components/EventListPage.vue'

export default {
  name: 'home',
  components: {
    EventListPage
  }
}
</script>
