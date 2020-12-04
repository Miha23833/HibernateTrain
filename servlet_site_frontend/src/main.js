import Vue from 'vue'
import App from './App.vue'
import Vuex from 'vuex'
import {ENTITY} from './enums/ENTITIES'
import {USER_ACTION} from "@/enums/USER_ACTIONS";

Vue.config.productionTip = false
Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        currentEntity: ENTITY.Customer,
        currentUserAction: USER_ACTION.SELECT,
        errorMessage: '',
    },
    getters: {
        getCurrentEntity(state) {
            return state.currentEntity;
        },
        getCurrentUserAction(state) {
            return state.currentUserAction;
        },
        getErrorMessage(state) {
            return state.errorMessage;
        },
    },
    mutations: {
        setCurrentEntity(state, payload) {
            state.currentEntity = payload;
        },

        setCurrentUserAction(state, payload) {
            state.currentUserAction = payload;
        },

        setErrorMessage(state, payload) {
            state.errorMessage = payload;
        }
    }
})

new Vue({
    render: h => h(App),
    store
}).$mount('#app')
