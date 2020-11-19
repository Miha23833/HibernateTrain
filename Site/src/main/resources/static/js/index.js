const headerButton = Vue.createApp({
    data() {
        return null;
    },
    methods: {
        getCustomersPage(event) {
            alert("getCustomersPage");
        },
        getProductsPage(event) {
            alert("getProductsPage");
        },
        getDealsPage(event) {
            alert("getDealsPage");
        }
    }
}).mount('#app');