Vue.createApp({
    data() {
        return {
            name: 'Vue.js'
        }
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
}).mount('#header-center');