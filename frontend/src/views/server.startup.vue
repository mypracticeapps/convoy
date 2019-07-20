<template>
  <div class="about">
    <h1>Server is starting up. Please wait</h1>
  </div>
</template>
<script>
  import PingAPI from '@/services/ping.api.js';

  export default {
    methods: {
      pingServer() {
        PingAPI.ping().then(() => {
          console.log("Starter started");
          this.$router.push({ name: 'home' })
        }, (response) => {
          if(error.response.status === 421){
            console.log("Server not started. retrying in one second");
            setTimeout(function () {
              this.pingServer()
            }.bind(this), 1000)
          }else {
            console.log("server failed with some other error");
            this.$router.push("error");
          }
        });
      }
    },
    mounted() {
      this.pingServer();
    }
  };
</script>
