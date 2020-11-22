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
                data: null
            }
        },
        methods: {
            get() {
                data = axios.post('/test', {
                        firstName: 'Finn',
                        lastName: 'Williams'
                    })
                    .then(response => (this.data = response.data));
            }
        }
    }).mount('#axios-get')
}

initComponent = function() {
    Vue.createApp({
        props: ['messages'],
        data() {
            return {
                messages = []
            }
        },
        template: '<div id="body">' +
            '<message-form :messages="messages" :messageAttr="message" />' +
            '<message-row v-for="message in messages" :key="message.id" :message="message" ' +
            ':editMethod="editMethod" :messages="messages" />' +
            '</div>',
        created() {
            axios.get('/test').then(response => {
                console.log(response);
                this.messages.push.apply(response.data)
            })
        },
        methods: {
            getMessages() {
                axios.get('/test').then(response => {
                    console.log(response);
                    this.messages.push.apply(response.data)
                })
            }
        }
    }).mount('#data-container')
}