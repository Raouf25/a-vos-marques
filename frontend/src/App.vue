<template>
    <v-app id="app" class="container">
        <div class="row">
            <div class="col-md-8">
                <!-- The map goes here -->
                <div id="map" class="map"></div>
            </div>

            <div class="col-md-4">
                <v-container fluid>
                    <v-row align="center">
                        <v-col cols="6">
                            <v-subheader>Prepended icon</v-subheader>
                        </v-col>
                        <v-col cols="6">
                            <v-select
                                    v-model="e1"
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
                        :color="'#ff1d5e'"
                />


                </div>







            </div>


        </div>
    </v-app>

</template>

<script>
    import api from './Api';
    import {OrbitSpinner} from 'epic-spinners'

    export default {

        components: { OrbitSpinner },

        name: 'app',

        data: () => {
            return {
                map: null,
                tileLayer: null,


                e1: 'Florida',

                loading: true,

                markerLayer: null,
                region: [],
                states: [],
                // states: [
                //     'Alabama', 'Alaska', 'American Samoa', 'Arizona',
                //     'Arkansas', 'California', 'Colorado', 'Connecticut',
                //     'Delaware', 'District of Columbia', 'Federated States of Micronesia',
                //     'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho',
                //     'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
                //     'Louisiana', 'Maine', 'Marshall Islands', 'Maryland',
                //     'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
                //     'Missouri', 'Montana', 'Nebraska', 'Nevada',
                //     'New Hampshire', 'New Jersey', 'New Mexico', 'New York',
                //     'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio',
                //     'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico',
                //     'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee',
                //     'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia',
                //     'Washington', 'West Virginia', 'Wisconsin', 'Wyoming',
                // ],


            }
        },


        mounted() { /* Code to run when app is mounted */
            this.initMap();
        },


        methods: { /* Any app-specific functions go here */
            initMap() {
                this.map = L.map('map').setView([46.603354, 1.8883335], 6);    //Paris 48.8566101,2.3514992    Toulouse 43.6044622,1.4442469
                this.tileLayer = L.tileLayer(
                    'https://cartodb-basemaps-{s}.global.ssl.fastly.net/rastertiles/voyager/{z}/{x}/{y}.png',
                    {
                        maxZoom: 18,
                        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>',
                    }
                );
                this.tileLayer.addTo(this.map);

                api.getAllTowns()
                    .then(response => {
                        // console.log(response.data);
                        this.region = response.data;
                        this.region.forEach((r) => {
                            this.states.push(r.valueOf().town);
                        });

                        // this.states.push(response.data.town)
                        // console.log(this.states)
                    })
                    .catch(error => {
                        // this.$log.debug(error)
                        this.error = "Failed to load todos"
                    })
                    .finally(() => this.loading = false);

                this.region.forEach((town) => {
                    this.states.push(town.name);
                });

            },

            changeRoute(a) {
                this.loading = true ;
                const newTown = this.region.filter(r => r.valueOf().town == a);
                console.log(a + ": " + newTown[0].latitude, newTown[0].longitude);

                api.getAll2(newTown[0].code)
                    .then(response => {
                        // this.todos = response.data;
                        var arrayOfLatLngs=[];

                        response.data
                            .filter(e => (e.latitude != null && e.longitude != null))
                            .forEach(e => {

                                console.log(e.name);

                                arrayOfLatLngs.push([e.latitude, e.longitude]);
                                // Add a marker to show where you clicked.
                                this.markerLayer = L.marker([e.latitude, e.longitude])
                                    .bindPopup(e.name)
                                    .addTo(this.map);

                                //     L.marker([e.latitude, e.longitude])
                                //      .bindPopup(e.name)
                                //      .removeFrom(this.map);
                            });

                        var bounds = new L.LatLngBounds(arrayOfLatLngs);

                        this.map.fitBounds(bounds);


                    })
                    .catch(error => {
                        // this.$log.debug(error)
                        this.error = "Failed to load todos"
                    })
                    .finally(() => this.loading = false);
            },
        },
    }
</script>

<style>
    /*#app {*/
    /*    font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
    /*    -webkit-font-smoothing: antialiased;*/
    /*    -moz-osx-font-smoothing: grayscale;*/
    /*    text-align: center;*/
    /*    color: #2c3e50;*/
    /*    margin-top: 60px;*/
    /*}*/

    .map {
        /*height: 600px;*/
        height: 800px;
    }


    .displayed{
        position: absolute;
        left: 50%;
        top: 50%;
    }

</style>
