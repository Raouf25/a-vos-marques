<template>
    <v-app id="app">
        <Header/>
        <div class="container">
            <v-card :elevation="24" class="mx-auto pa-6 resize">
                <router-view/>
            </v-card>
        </div>
        <Footer/>
    </v-app>
</template>

<script>
    import Header from '@/components/Header.vue';
    import Footer from '@/components/Footer.vue';

    export default {

        components: {
            Header, Footer
        },

    }
</script>

<style>
    .resize {
        max-width: 1140px !important;
        margin: auto !important;
    }
</style>
