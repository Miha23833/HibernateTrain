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
        errorMessages: {},
    },
    getters: {
        getCurrentEntity(state) {
            return state.currentEntity;
        },
        getCurrentUserAction(state) {
            return state.currentUserAction;
        },
        getErrorMessages(state) {
            return state.errorMessages;
        },
    },
    mutations: {
        setCurrentEntity(state, payload) {
            state.currentEntity = payload;
        },

        setCurrentUserAction(state, payload) {
            state.currentUserAction = payload;
        },

        addErrorMessage(state, payload) {
            let key = new Date().getTime()
            payload['key'] = key;
            Vue.set(state.errorMessages, key, payload);
        },

        removeErrorMessageByKey(state, payload) {
            // delete state.errorMessages[payload];
            Vue.delete(state.errorMessages, payload)
        }
    }
})

new Vue({
    render: h => h(App),
    store
}).$mount('#app')
