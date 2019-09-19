import axios from 'axios'

// const SERVER_URL = 'http://localhost:9000/go-api';
// const SERVER_URL = 'http://localhost:8080';
const SERVER_URL = '/';

const instance = axios.create({
    baseURL: SERVER_URL,
    timeout: 150000
});

export default {
    // (C)reate
    createNew: (text, completed) => instance.post('todos', {title: text, completed: completed}),
    // (R)ead
    getAll: () => instance.get('stadiums/31', {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getEventPerTown: (code) => instance.get('event/'+code, {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getAll2: (code) => instance.get('stadiums/'+code, {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),

    getAllTowns: () => instance.get('regions', {
        transformResponse: [function (data) {
            return data? JSON.parse(data) : data;
        }]
    }),
    // (U)pdate
    updateForId: (id, text, completed) => instance.put('todos/'+id, {title: text, completed: completed}),
    // (D)elete
    removeForId: (id) => instance.delete('todos/'+id)
}