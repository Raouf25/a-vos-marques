<template>
    <v-app id="app">
      <div id="nav">
          <a href="#" @click.prevent="$router.push({name:'home'})">Home</a>  |
          <a href="#" @click.prevent="$router.push({name:'about'})">About</a>
      </div>
      <router-view/>
    </v-app>
</template>
