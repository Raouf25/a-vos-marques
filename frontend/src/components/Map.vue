<template>
    <div id="map" class="map"></div>
</template>

<script>
    import L from 'leaflet';

    export default {
        data: () => {
            return {
                map: null,
                tileLayer: null,
                e1: 'Florida',
                layerGroup: null,
            }
        },


        mounted() {
            this.initMap();
        },

        methods: {

            initMap() {
                var mapOptions = {
                    center: [46.603354, 1.8883335],
                    zoom: 6
                };

                this.map = L.map('map', mapOptions);
                this.tileLayer = L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/rastertiles/voyager/{z}/{x}/{y}.png');
                this.tileLayer.addTo(this.map);
            },

            updateMarker() {

                var arrayOfLatLngs0 = this.gpsCoordinatesList.map(e => [e.latitude, e.longitude]);


                var bounds = new L.LatLngBounds(arrayOfLatLngs0);
                this.map.fitBounds(bounds);

                if (this.layerGroup != null) {
                    this.map.removeLayer(this.layerGroup);
                }

                var arrayOfLatLngs = this.gpsCoordinatesList.map(e => L.marker([e.latitude, e.longitude]).bindPopup(e.name));

                // Creating layer group
                this.layerGroup = L.layerGroup(arrayOfLatLngs).addTo(this.map);    // Adding layer group to map
            },


        },

        props: {
            gpsCoordinatesList: Array
        },

        watch: {
            'gpsCoordinatesList': function () {
                this.updateMarker();
            }
        },

    }
</script>

<style>
    .map {
        height: 450px;
    }
</style>
