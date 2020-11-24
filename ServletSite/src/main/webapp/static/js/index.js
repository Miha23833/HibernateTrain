getHeaderButtonsActions = function() {
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
}

getDataToMain = function() {
    Vue.createApp({
        data() {
            return {
                items: [{ message: 'Foo' }, { message: 'Bar' }]
            }
        }
    }).mount('#array-rendering')
}

getData = function() {
    Vue.createApp({
        data() {
            return {
                queryParams: {},
                serverData: []
            }
        },
        methods: {
            getServerData() {
                let queryParams = this.queryParams
                axios.post('/get-data/Customers', {
                    queryParams
                }).then(resp => this.serverData.push(resp.data))
            }
        }
    }).mount('#filter')
}