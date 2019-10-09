<template>
    <div class="footer">
        <div class="left">
            copyright &copy; {{ new Date().getFullYear() }}
        </div>
        <div class="right">
            <a href="#" @click.prevent="$router.push({name:'about'})">About US</a>
            <v-btn class="mx-4 " icon>
                <v-icon size="24px">fab fa-facebook</v-icon>
            </v-btn>
            <v-btn class="mx-4 " icon>
                <v-icon size="24px">fab fa-twitter</v-icon>
            </v-btn>
            <v-btn class="mx-4 " icon>
                <v-icon size="24px">fab fa-instagram</v-icon>
            </v-btn>
        </div>
    </div>
</template>

<script>

</script>

<style>
    .footer {
        bottom: 8px;
        max-width: 1140px !important;
        margin: auto !important;


    }

    .footer div {
        font-size: 12px;
        text-transform: uppercase;
        font-weight: 500;
        padding: 15px;
        text-decoration: none;

        display: inline-block;
    }

    .footer div a {
        color: inherit !important;
    }
</style>
