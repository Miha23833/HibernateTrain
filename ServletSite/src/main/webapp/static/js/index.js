const staticServerData = Vue.reactive({
    columns: [],
    response: []
})
const filterData = Vue.reactive({
    queryParams: {},
    filterTemplate: "",
    filterMapping: ""
})

let headerButtonsComponent = Vue.createApp({
    data() {
        return {
            name: 'Vue.js'
        }
    },
    methods: {
        getCustomersPage(event) {
            filterData.queryParams = {};
            staticServerData.columns = [];
            staticServerData.response = [];
            filterData.filterTemplate = "Customers";
            filterData.filterMapping = "/get-data/Customers";
        },
        getProductsPage(event) {
            filterData.queryParams = {};
            staticServerData.columns = [];
            staticServerData.response = [];
            filterData.filterTemplate = "Products";
            filterData.filterMapping = "/get-data/Products";
        },
        getDealsPage(event) {
            filterData.queryParams = {};
            staticServerData.columns = [];
            staticServerData.response = [];
            filterData.filterTemplate = "Deals";
            filterData.filterMapping = "/get-data/Deals";
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
            let queryParams = this.queryParams;
            axios.post(this.filterMapping, {
                queryParams
            }).then(resp => {
                resp.data.response.forEach(
                    (elem) => staticServerData.response.push(elem)
                )
                staticServerData.columns = resp.data.columns;
            });
        }
    }
}).mount('#filter');