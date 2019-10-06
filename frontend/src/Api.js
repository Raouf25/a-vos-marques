import axios from 'axios'



export default {
    // (R)ead
    getEventData: (code) => axios.get('event/data/'+code, {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getEventPerTown: (code) => axios.get('event/'+code, {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getAllStadiums: (code) => axios.get('stadiums/'+code, {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getAllTowns: () => axios.get('regions', {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    })
}
