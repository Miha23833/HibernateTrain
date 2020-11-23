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
                data: null,
                pressedCount: 0,
                serverData: []
            }
        },
        methods: {
            get() {
                data = axios.post('/test', {
                        firstName: 'Finn',
                        lastName: 'Williams'
                    })
                    .then(response => (this.data = response.data));
            },
            getServerData() {
                axios.post('/get-data/Customers', {
                    params: {
                        val1: "val1",
                        val2: "val2",
                        val3: "val3",
                        val4: "val4",
                        val5: "val5"
                    }
                }).then(resp => this.serverData = resp.data)
            }
        }
    }).mount('#axios-get')
}