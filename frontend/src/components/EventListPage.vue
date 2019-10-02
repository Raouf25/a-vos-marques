<template>
    <div class="container" id="eventListPage">
        <v-card :elevation="24"
                class="mx-auto pa-6">
            <div class="row">
                <div class="col-md-5">
                    <div id="map" class="map"></div>
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
        </v-card>
    </div>
</template>

<script>
    import api from '../Api';

    export default {
        data: () => {
            return {
                map: null,
                tileLayer: null,
                e1: 'Florida',
                layerGroup: null,
                markerLayer: null,
                region: [],
                states: [],

                items: [],
                headers: [
                    {text: 'id', value: 'idEvent'},
                    {text: 'Title event', value: 'titleEvent'},
                    {text: 'Date event', value: 'dateEvent'},
                ],
            }
        },


        mounted() {
            this.initMap();
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

            initMap() {
                // // Creating map options
                var mapOptions = {
                    center: [46.603354, 1.8883335],
                    zoom: 6
                };

                this.map = L.map('map', mapOptions);
                this.tileLayer = L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/rastertiles/voyager/{z}/{x}/{y}.png');
                this.tileLayer.addTo(this.map);
            },

            changeTown(a) {
                const newTown = this.region.filter(r => r.valueOf().town == a);
                var arrayOfLatLngs0 = [];
                var arrayOfLatLngs = [];
                api.getAllStadiums(newTown[0].code)
                    .then(response => {
                        response.data
                            .filter(e => (e.latitude != null && e.longitude != null))
                            .forEach(e => {
                                arrayOfLatLngs0.push([e.latitude, e.longitude]);
                                arrayOfLatLngs.push(L.marker([e.latitude, e.longitude]).bindPopup(e.name));
                            });

                        var bounds = new L.LatLngBounds(arrayOfLatLngs0);
                        this.map.fitBounds(bounds);

                        if (this.layerGroup != null) {
                            this.map.removeLayer(this.layerGroup);
                        }

                        // Creating layer group
                        this.layerGroup = L.layerGroup(arrayOfLatLngs);
                        this.layerGroup.addTo(this.map);    // Adding layer group to map

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
    .map {
        height: 450px;
    }
</style>
