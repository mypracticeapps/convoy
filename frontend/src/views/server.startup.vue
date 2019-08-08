<template>
  <div class="about center">
    <h1>Server is starting up. Please wait</h1>
  </div>
</template>
<script>
    import { ajax } from 'rxjs/ajax';
    import {
        retryWhen,
        delay,
    } from 'rxjs/operators';

    export default {
        methods: {
            pingServer() {
                ajax(`http://localhost:8080/api/v1/ping`)
                    .pipe(retryWhen(errors => errors.pipe(delay(2000)))) // retry every 2 seconds
                    .subscribe(()=>{
                        this.$router.push({name: 'home'});
                    });
            }
        },
        mounted() {
            this.pingServer();
        }
    };
</script>
