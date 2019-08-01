<template>
  <div class="about">
    <h1>Server is starting up. Please wait</h1>
  </div>
</template>
<script>
    import PingAPI from '@/services/ping.api.js';
    import { ajax } from 'rxjs/ajax';
    import {timer, combineLatest, interval, of, Subject, defer} from 'rxjs';
    import {
        retryWhen,
        delayWhen,
        retry,
        catchError,
        map,
        share,
        startWith,
        switchMap,
        tap,
        delay,
        pairwise
    } from 'rxjs/operators';

    export default {
        methods: {
            pingServer() {
                ajax(`http://localhost:8080/api/v1/ping`)
                    .pipe(retryWhen(errors => errors.pipe(delay(2000)))) // retry every 2 seconds
                    .subscribe(()=>{
                        this.$router.push({name: 'home'});
                    });

                // PingAPI.ping()
                //     .pipe(catchError(error => {
                //         console.log(error);
                //         return Promise.reject(error)
                //     }))
                //     .pipe(retry(3))
                // .pipe(retryWhen(errors => {
                //     return timer(2000).pipe(tap(console.log));
                // }))
                // .subscribe(console.log);
            }
        },
        mounted() {
            this.pingServer();
        }
    };
</script>
