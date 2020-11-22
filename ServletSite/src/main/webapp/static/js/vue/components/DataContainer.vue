<template>
    <div id="body">
        <p>{{ message }}</p>
    </div>
</template>

<script>
export default {
    data(){
        this.message = "some message"
    },
    methods: {
        getMessages() {
            axios.get('/message').then(responce => this.message = responce.data);
        }
    },
    created(){
        this.getMessages();
    }    
}
</script>

