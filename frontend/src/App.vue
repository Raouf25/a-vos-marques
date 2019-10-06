<template>
    <v-app id="app">
      <div id="nav">
          <a href="#" @click.prevent="$router.push({name:'home'})">Home</a>  |
          <a href="#" @click.prevent="$router.push({name:'about'})">About</a>
      </div>
        <div class="container">
            <v-card :elevation="24" class="mx-auto pa-6">
                <router-view/>
            </v-card>
        </div>
    </v-app>
</template>
