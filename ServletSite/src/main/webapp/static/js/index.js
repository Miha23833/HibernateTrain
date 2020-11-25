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
            axios.get('/get-data/Customers').then(resp => {
                filterData.filterTemplate = resp.data.filterTemplate;
                filterData.filterMapping = resp.data.filterMapping;
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
            let queryParams = this.queryParams;
            axios.post(this.filterMapping, {
                queryParams
            }).then(resp => {
                resp.data.response.forEach(
                    (elem) => staticServerData.response.push(elem)
                )
                staticServerData.columns = resp.data.columns;
            });
            render();
        }
    }
}).mount('#filter')