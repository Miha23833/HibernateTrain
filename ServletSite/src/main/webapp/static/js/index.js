const staticServerData = Vue.reactive({
    columns: [],
    response: []
})
const filterData = Vue.reactive({
    queryParams: {},
    filterTemplate: ""
})

let headerButtonsComponent = Vue.createApp({
    data() {
        return {
            name: 'Vue.js'
        }
    },
    methods: {
        getCustomersPage(event) {
            axios.get('/get-data/Customers').then(resp => {
                filterData.filterTemplate = resp.data.filterTemplate;
            })
        },
        getProductsPage(event) {
            alert("getProductsPage");
        },
        getDealsPage(event) {
            alert("getDealsPage");
        }
    }
}).mount('#header-center');

let mainDataComponent = Vue.createApp({
    data() {
        return staticServerData
    }
}).mount('#main-data')

let filterComponent = Vue.createApp({
    data() {
        return filterData
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