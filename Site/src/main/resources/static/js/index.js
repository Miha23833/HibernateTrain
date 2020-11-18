const hello = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue 3!'
        }
    },
    methods: {
        setMessage(event) {
            this.message = event.target.value;
        }
    }
}).mount('#app');

const timer = Vue.createApp({
    data() {
        return {
            message: 'SomeMSG'
        }
    },
    methods: {
        setTimeWithTimeOut() {
            setTimeout(this.setTime, 200, null);
        },
        setTime(event) {
            this.message = Date.now();
        }
    }
}).mount('#myDiv')