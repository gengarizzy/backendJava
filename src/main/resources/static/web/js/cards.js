Vue.createApp({

    data() {
        return {
            clientInfo: {},
            creditCards: [],
            debitCards: [],
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {






        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT");
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT");


                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },

        isCardExpired: function(card) {
            const thruDateObj = new Date(card.thruDate);
            const DateObj = new Date();
            return thruDateObj < DateObj;
        },

         deleteCards: function() {
                axios.delete("/api/clients/current/cards")
                    .then(response => {
                        // Manejar la respuesta del servidor después de eliminar las tarjetas.
                        // Por ejemplo, puedes mostrar un mensaje de éxito o actualizar la lista de tarjetas.
                        console.log(response.data); // Mensaje de éxito del servidor
                    })
                    .catch(error => {
                        // Manejar errores en caso de que la eliminación falle.
                        console.error(error);
                    });
            }

    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')