<template>
    <v-app>
        <div class="container">
            <v-card :elevation="hover ? 24 : 6"
                    class="mx-auto pa-6">
                <div class="row">
                    <div class="col-md-5">
                        <!-- The map goes here -->
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
                                              v-on:input="changeRoute(`${e1}`)"
                                              single-line
                                    ></v-select>
                                </v-col>
                            </v-row>
                        </v-container>
                        <div v-if="loading">
                            <orbit-spinner class="displayed"
                                           :animation-duration="1200"
                                           :size="55"
                                           :color="'#ff1d5e'"/>
                        </div>
                        <v-data-table :headers="headers"
                                      :items="items"
                                      :items-per-page="5"
                                      class="elevation-1">
                        </v-data-table>
                    </div>
                </div>
            </v-card>
        </div>
    </v-app>
</template>

<script>
    // import Footer from './components/Footer';
    import api from './Api';
    import {OrbitSpinner} from 'epic-spinners'

    export default {

        // components: {OrbitSpinner, Footer},
        components: {OrbitSpinner},

        name: 'app',

        data: () => {
            return {
                map: null,
                tileLayer: null,
                e1: 'Florida',
                loading: true,
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


        methods: {
            initMap() {

                // // Creating map options
                var mapOptions = {
                    center: [46.603354, 1.8883335],  //Paris 48.8566101,2.3514992    Toulouse 43.6044622,1.4442469
                    zoom: 6
                };

                this.map = L.map('map', mapOptions);
                this.tileLayer = L.tileLayer(
                    'https://cartodb-basemaps-{s}.global.ssl.fastly.net/rastertiles/voyager/{z}/{x}/{y}.png'
                    // ,
                    // {
                    //     maxZoom: 18,
                    //     attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>',
                    // }
                );
                this.tileLayer.addTo(this.map);

                // var layer = new L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
                // this.map.addLayer(layer);      // Adding layer to the map

                api.getAllTowns()
                    .then(response => {
                        this.region = response.data;
                        this.states = response.data.map(r => r.valueOf().town);
                    })
                    .catch(error => {
                        // this.$log.debug(error)
                        this.error = "Failed to load todos"
                    })
                    .finally(() => this.loading = false);
            },

            changeRoute(a) {
                this.loading = true;
                const newTown = this.region.filter(r => r.valueOf().town == a);
                var arrayOfLatLngs0 = [];
                var arrayOfLatLngs = [];
                api.getAll2(newTown[0].code)
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

                    })
                    .catch(error => {
                        // this.$log.debug(error)
                        this.error = "Failed to load todos"
                    })
                    .finally(() => this.loading = false);
                this.getEvent(a);
            },


            getEvent(a) {
                this.loading = true;
                const newTown = this.region.filter(r => r.valueOf().town == a);
                api.getEventPerTown(newTown[0].code.valueOf())
                    .then(response => {
                        this.items = response.data
                            .map((e, idx) => ({
                                idEvent: idx + 1,
                                titleEvent: e.title,
                                dateEvent: e.dateDeDebut,
                            }));
                    })
                    .catch(error => {
                        this.error = "Failed to load todos"
                    })
                    .finally(() => this.loading = false);
            },


        },
    }
</script>

<style>
    .map {
        /*height: 600px;*/
        height: 450px;
    }

    .displayed {
        position: absolute;
        left: 50%;
        top: 50%;
    }
</style>
