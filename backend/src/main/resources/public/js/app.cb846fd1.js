(function(t){function e(e){for(var a,i,s=e[0],l=e[1],u=e[2],p=0,d=[];p<s.length;p++)i=s[p],Object.prototype.hasOwnProperty.call(o,i)&&o[i]&&d.push(o[i][0]),o[i]=0;for(a in l)Object.prototype.hasOwnProperty.call(l,a)&&(t[a]=l[a]);c&&c(e);while(d.length)d.shift()();return r.push.apply(r,u||[]),n()}function n(){for(var t,e=0;e<r.length;e++){for(var n=r[e],a=!0,s=1;s<n.length;s++){var l=n[s];0!==o[l]&&(a=!1)}a&&(r.splice(e--,1),t=i(i.s=n[0]))}return t}var a={},o={app:0},r=[];function i(e){if(a[e])return a[e].exports;var n=a[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,i),n.l=!0,n.exports}i.m=t,i.c=a,i.d=function(t,e,n){i.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},i.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},i.t=function(t,e){if(1&e&&(t=i(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(i.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)i.d(n,a,function(e){return t[e]}.bind(null,a));return n},i.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return i.d(e,"a",e),e},i.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},i.p="/";var s=window["webpackJsonp"]=window["webpackJsonp"]||[],l=s.push.bind(s);s.push=e,s=s.slice();for(var u=0;u<s.length;u++)e(s[u]);var c=l;r.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var a=n("1356"),o=n.n(a);o.a},1356:function(t,e,n){},"56d7":function(t,e,n){"use strict";n.r(e);var a=n("2b0e"),o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-app",{staticClass:"container",attrs:{id:"app"}},[n("div",{staticClass:"row"},[n("div",{staticClass:"col-md-8"},[n("div",{staticClass:"map",attrs:{id:"map"}})]),n("div",{staticClass:"col-md-4"},[n("v-container",{attrs:{fluid:""}},[n("v-row",{attrs:{align:"center"}},[n("v-col",{attrs:{cols:"6"}},[n("v-subheader",[t._v("Prepended icon")])],1),n("v-col",{attrs:{cols:"6"}},[n("v-select",{attrs:{items:t.states,"menu-props":"auto",label:"Select","hide-details":"","prepend-icon":"map","single-line":""},on:{input:function(e){return t.changeRoute(""+t.e1)}},model:{value:t.e1,callback:function(e){t.e1=e},expression:"e1"}})],1)],1)],1),t.loading?n("div",[n("orbit-spinner",{staticClass:"displayed",attrs:{"animation-duration":1200,size:55,color:"#ff1d5e"}})],1):t._e()],1)])])},r=[],i=n("bc3a"),s=n.n(i);const l="/",u=s.a.create({baseURL:l,timeout:15e4});var c={createNew:(t,e)=>u.post("todos",{title:t,completed:e}),getAll:()=>u.get("stadiums/31",{transformResponse:[function(t){return t?JSON.parse(t):t}]}),getAll2:t=>u.get("stadiums/"+t,{transformResponse:[function(t){return t?JSON.parse(t):t}]}),getAllTowns:()=>u.get("regions",{transformResponse:[function(t){return t?JSON.parse(t):t}]}),updateForId:(t,e,n)=>u.put("todos/"+t,{title:e,completed:n}),removeForId:t=>u.delete("todos/"+t)},p=n("4583"),d={components:{OrbitSpinner:p["a"]},name:"app",data:()=>{return{map:null,tileLayer:null,e1:"Florida",loading:!0,markerLayer:null,region:[],states:[]}},mounted(){this.initMap()},methods:{initMap(){this.map=L.map("map").setView([46.603354,1.8883335],6),this.tileLayer=L.tileLayer("https://cartodb-basemaps-{s}.global.ssl.fastly.net/rastertiles/voyager/{z}/{x}/{y}.png",{maxZoom:18,attribution:'&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, &copy; <a href="https://carto.com/attribution">CARTO</a>'}),this.tileLayer.addTo(this.map),c.getAllTowns().then(t=>{this.region=t.data,this.region.forEach(t=>{this.states.push(t.valueOf().town)})}).catch(t=>{this.error="Failed to load todos"}).finally(()=>this.loading=!1),this.region.forEach(t=>{this.states.push(t.name)})},changeRoute(t){this.loading=!0;const e=this.region.filter(e=>e.valueOf().town==t);console.log(t+": "+e[0].latitude,e[0].longitude),c.getAll2(e[0].code).then(t=>{var e=[];t.data.filter(t=>null!=t.latitude&&null!=t.longitude).forEach(t=>{console.log(t.name),e.push([t.latitude,t.longitude]),this.markerLayer=L.marker([t.latitude,t.longitude]).bindPopup(t.name).addTo(this.map)});var n=new L.LatLngBounds(e);this.map.fitBounds(n)}).catch(t=>{this.error="Failed to load todos"}).finally(()=>this.loading=!1)}}},f=d,h=(n("034f"),n("2877")),m=n("6544"),g=n.n(m),v=n("7496"),b=n("62ad"),y=n("a523"),w=n("0fd9"),O=n("b974"),S=n("e0c7"),j=Object(h["a"])(f,o,r,!1,null,null,null),x=j.exports;g()(j,{VApp:v["a"],VCol:b["a"],VContainer:y["a"],VRow:w["a"],VSelect:O["a"],VSubheader:S["a"]});var _=n("f309");a["a"].use(_["a"]);var C=new _["a"]({icons:{iconfont:"mdi"}});a["a"].config.productionTip=!1,new a["a"]({vuetify:C,render:t=>t(x)}).$mount("#app")}});
//# sourceMappingURL=app.cb846fd1.js.map