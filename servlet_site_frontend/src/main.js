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
  mutations: {
    setCurrentEntity(state, payload){
      if (payload in ENTITY) {
        state.currentEntity = payload;
      }
    },

    setCurrentUserAction(state, payload){
      if (payload in USER_ACTION){
        state.currentUserAction = payload;
      }
    },

    setErrorMessage(state, payload){
      if (payload instanceof String){
        state.errorMessage = payload;
      }
    }
  }

}  )

new Vue({
  render: h => h(App),
}).$mount('#app')
