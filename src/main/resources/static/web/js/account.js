Vue.createApp({

    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/accounts/${id}`)
                .then((response) => {
                    //get client ifo
                    this.accountInfo = response.data;
                    this.accountInfo.transactions.sort((a, b) => parseInt(b.id - a.id))
                })
                .catch((error) => {
                    // handle error
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
   deleteAccount: function () {

           const urlParams = new URLSearchParams(window.location.search);
                      const id = urlParams.get('id');


//Se supone que esto va al controller, donde llamo a la logica para cambiar el estado de la cuenta
//Pero sigo entrando al catch
           axios.post(`/api/clients/current/accounts/${id}`)
               .then(response => {
                   // Manejar la eliminación exitosa, redirigir al usuario o realizar otras acciones necesarias
                   window.location.href = "/web/accounts.html";
               })

               //POR QUE NO ENTRO AL .THEN?


               .catch(error => {
                   // Manejar errores en la eliminación de la cuenta
                   console.error("CATCH CONSOLE ERROR. Error al eliminar la cuenta");
                   this.errorMsg = "deleteAccount CATCH ! Account deleting error";
                   this.errorToats.show();
               });
       },

    },

    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')