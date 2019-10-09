<template>
    <div>
        <v-toolbar>
            <img class="mr-3" src="./../assets/run_logo.png" height="40"  @click.prevent="$router.push({name:'home'})"/>
            <v-toolbar-title @click.prevent="$router.push({name:'home'})">Run For Life</v-toolbar-title>
            <div class="flex-grow-1"></div>
            <v-toolbar-items>
                <v-btn class="mx-4 " icon>
                    <v-icon size="24px">fas fa-user</v-icon>
                    <span>Login</span>
                </v-btn>
            </v-toolbar-items>
        </v-toolbar>
    </div>
</template>

<script>

</script>

<style>
    /*The !important rule at the end of a value will override any other style declarations of that attribute, including inline styles.*/
    .v-toolbar__content {
        max-width: 1140px !important;
        margin: auto !important;
    }
</style>
