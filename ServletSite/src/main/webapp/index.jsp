<!DOCTYPE html>
<html lang="ru" xmlns:v-on="http://www.w3.org/1999/xhtml">

<head>
    <title>Изучаем Vue 3</title>
    <meta charset="utf-8" />
    <script src="https://unpkg.com/vue@next"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>

    <script type="text/javascript" src="/js/index.js"></script>
    <link type="text/css" rel="stylesheet" href="/css/index.css" />
</head>

<body bgcolor="whitesmoke">
    <header>
        <div id="header-left">
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/1200px-Amazon_logo.svg.png" alt="normal" />
        </div>
        <div id="header-center">
            <ul>
                <li><button @click="getCustomersPage">Customers</button></li>
                <li><button @click="getProductsPage">Products</button></li>
                <li><button @click="getDealsPage">Deals</button></li>
            </ul>
        </div>
        <script>
            getHeaderButtonsActions()
        </script>
        <div id="header-right">
            <button>Log in</button>
        </div>
    </header>

    <main>
        <div id="axios-get">
            <button @click="get">Customers</button> {{data}}
        </div>
        <script>
            getData()
        </script>
        <!-- <div id="app">
        <input type="text" v-on:input="setMessage" />
        <p>{{message}}</p>
    </div>

    <div id="myDiv">
        <input type="button" value="update time!" v-on:click="setTime">
        <p>{{message}}</p>
    </div>
    <script type="text/javascript" src="js/index.js"></script> -->
    </main>
</body>

</html>