<template>
    <div class="row">
        <h3>
            CALENDRIER DES COURSES
        </h3>
        <h4 class="discription">
            Découvrez votre prochaine course et son parcours, inscrivez-vous en ligne ou consultez les résultats d'une épreuve.
            Donnez votre avis sur une course et publiez vos photos et vidéos.
        </h4>
        <div class="col-md-5">
            <Map :gpsCoordinatesList="arrayOfLatLngs0"/>
        </div>
        <div class="col-md-7">
            <v-container fluid>
                <v-row align="center">
                    <v-col cols="6">
                        <v-subheader>Ville</v-subheader>
                    </v-col>
                    <v-col cols="6">
                        <v-select v-model="e1"
                                  :items="states"
                                  menu-props="auto"
                                  label="Select"
                                  hide-details
                                  prepend-icon="map"
                                  v-on:input="changeTown(`${e1}`)"
                                  single-line
                        ></v-select>
                    </v-col>
                </v-row>
            </v-container>
            <v-data-table :headers="headers"
                          :items="items"
                          :items-per-page="5"
                          class="elevation-1">
                <template v-slot:item.titleEvent="{ item }">
                    <a href="#" @click.prevent="$router.push({name:'comp',params:{code: item.code}})">{{ item.titleEvent }}</a>
                </template>
            </v-data-table>
        </div>
    </div>
</template>

<script>
    import api from '../Api';
    import Map from '@/components/Map.vue';

    export default {

        components: {
            Map
        },

        data: () => {
            return {
                arrayOfLatLngs0: [],

                e1: 'Florida',
                region: [],
                states: [],

                items: [],
                headers: [
                    {text: 'id', value: 'idEvent'},
                    {text: 'type', value: 'type'},
                    {text: 'Title event', value: 'titleEvent'},
                    {text: 'Date event', value: 'dateEvent'},
                ],
            }
        },


        created() {
            this.getAllTowns();
        },

        methods: {
            formatDate(date) {
                var day = new Date(date);
                var dd = day.getDate();
                var mm = day.getMonth() + 1;
                var yyyy = day.getFullYear();

                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;

                day = dd + '/' + mm + '/' + yyyy;
                return day;
            },


            getAllTowns() {
                api.getAllTowns()
                    .then(response => {
                        this.region = response.data;
                        this.states = response.data.map(r => r.valueOf().town);
                    });
            },


            changeTown(a) {
                const newTown = this.region.filter(r => r.valueOf().town == a);
                api.getAllStadiums(newTown[0].code)
                    .then(response => {
                        response.data
                            .filter(e => (e.latitude != null && e.longitude != null))
                            .forEach(e => {
                                this.arrayOfLatLngs0.push({
                                    'latitude': e.latitude,
                                    'longitude': e.longitude,
                                    'name': e.name
                                });
                            });


                    });
                this.getEvent(a);
            },

            getEvent(a) {
                const newTown = this.region.filter(r => r.valueOf().town == a);
                api.getEventPerTown(newTown[0].code.valueOf())
                    .then(response => {
                        this.items = response.data
                            .map((e, idx) => ({
                                idEvent: idx + 1,
                                type: e.type,
                                titleEvent: e.title,
                                code: e.code,
                                dateEvent: this.formatDate(e.dateDeDebut),
                            }));
                    });
            },
        },
    }
</script>

<style>
    .discription{
        font-size: 1.125rem;
        line-height: 1.5em;
        font-family: "Roboto", "Helvetica", "Arial", sans-serif;
        font-weight: 300;
        text-align: center!important;
    }
</style>
