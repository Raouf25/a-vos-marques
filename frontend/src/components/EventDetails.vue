<template>
 <div class="row">
  <div class="col-md-5">
   <Map :gpsCoordinatesList="arrayOfLatLngs0"/>
  </div>
  <div class="col-md-7">
   <h1> {{this.event.title}}
<!--    Event Details {{ $route.params.code }}: -->
   </h1>
   <div class="row">
    <div class="col-sm">
     <p>league: {{this.event.league}} </p>
     <p>type: {{this.event.type}} </p>
     <p>recompenses: {{this.event.recompenses}} </p>
     <p>conditions: {{this.event.conditions}} </p>
     <p>avisTechniqueEtSecurite: {{this.event.avisTechniqueEtSecurite}} </p>
     <p>montantInscription: {{this.event.montantInscription}} </p>
     <p>certificatDeMesurage: {{this.event.certificatDeMesurage}} </p>
     <p>autresInfos :{{this.event.autresInfos}} </p>
     <p>officiel: {{this.event.officiel}} </p>
    </div>
    <div class="col-sm">
     <p>infosEpreuve:{{this.event.infosEpreuve}}</p>
     <p>challenge:{{this.event.challenge}}</p>
     <p>contactTechnique:{{this.event.contactTechnique}}</p>
     <p>epreuves:{{this.event.epreuves}}</p>
     <p>services:{{this.event.services}}</p>
     <p>niveau:{{this.event.niveau}}</p>
     <p>arrivee:{{this.event.arrivee}}</p>
     <p>depart:{{this.event.depart}}</p>
     <p>dateDeDebut : {{formatDate(this.event.dateDeDebut)}}</p>
     <p>dateDeFin:{{formatDate(this.event.dateDeFin)}}</p>
    </div>
   </div>
   <div class="row right">
    <v-btn small color="primary">Inscription</v-btn>
   </div>
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
    event: '',
   }
  },

  mounted() {
   this.getEventData(this.$route.params.code);
  },

  methods: {
   getEventData(code) {
    api.getEventData(parseInt(code))
            .then(response => {
             this.event = response.data;
             if (this.event.stadiumAddress !== null) {
              this.arrayOfLatLngs0.push({
               'latitude': this.event.stadiumAddress.latitude,
               'longitude': this.event.stadiumAddress.longitude,
               'name': this.event.stadiumAddress.name
              });
             }
            });
   },

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
  },
 }
</script>

<style>
</style>
