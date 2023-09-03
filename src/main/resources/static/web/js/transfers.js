Vue.createApp({
    data() {
        return {
                       clientAccounts: [],
                       clientAccountsTo: [],
                       debitCards: [],
                       errorToats: null,
                       errorMsg: null,
                       accountFromNumber: "VIN",
                       accountToNumber: "VIN",
                       trasnferType: "own",
                       amount: 0,
                       description: ""
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientAccounts = response.data.accounts;
                })
                .catch((error) => {
                    console.log(error);
                    this.errorMsg = "axios.get(/api/clients/current/accounts) (getData function)";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkTransfer: function () {
            if (this.accountFromNumber == "VIN") {
                this.errorMsg = "You must select an origin account";
                this.errorToats.show();
            }
            else if (this.accountToNumber == "VIN") {
                this.errorMsg = "You must select a destination account";
                this.errorToats.show();
            } else if (this.amount == 0) {
                this.errorMsg = "You must indicate an amount";
                this.errorToats.show();
            }
            else if (this.description.length <= 0) {
                this.errorMsg = "You must indicate a description";
                this.errorToats.show();
            } else {
                this.modal.show();
            }
        },
        transfer: function () {
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
axios.post(`/api/transactions?
originAccountNumber=${this.accountFromNumber}
&destinationAccountNumber=${this.accountToNumber}
&amount=${this.amount}&
description=${this.description}`

, config)                .then(response => {
                    this.modal.hide();
                    this.okmodal.show();
                })
                .catch((error) => {
                    console.log(POST TRANSACTION ERROR CATCH);
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        changedType: function () {
            this.originAccountNumber = "VIN";
            this.destinationAccountNumber = "VIN";
        },
        changedFrom: function () {
            if (this.trasnferType    == "own") {
                this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.originAccountNumber);
                this.destinationAccountNumber = "VIN";
            }
        },
        finish: function () {
            window.location.reload();
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {

                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
    }
}).mount("#app");