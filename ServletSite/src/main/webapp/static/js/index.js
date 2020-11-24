let headerButtonsComponent = Vue.createApp({
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

const staticServerData = Vue.reactive({
    columns: [],
    response: []
})

let mainDataComponent = Vue.createApp({
    data() {
        return staticServerData
    }
}).mount('#main-data')

let filterComponent = Vue.createApp({
    components: {
        mainDataComponent
    },
    data() {
        return {
            queryParams: {}
        }
    },
    methods: {
        getServerData() {
            let queryParams = this.queryParams
            axios.post('/get-data/Customers', {
                queryParams
            }).then(resp => {
                resp.data.response.forEach(
                    (elem) => staticServerData.response.push(elem)
                )
                staticServerData.columns = resp.data.columns;
            })
        }
    }
}).mount('#filter')