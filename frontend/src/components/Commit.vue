<template>
  <div class="card commit">
    <div class="card-body commit-body">
      <div class="commit-info">
        <p class="font-weight-bold text-left mb-1">{{commit.message}}</p>
        <p class="font-weight-normal text-left author-info">
          committed on {{commit.committer.time.substring(0,10)}} by {{commit.committer.name}}
        </p>
      </div>
      <div class="commit-meta-data">
        <p class="font-weight-normal text-right">{{commit.id.substring(0,7)}}</p>
        <!--              <p class="font-weight-normal text-right">US123456</p>-->
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Commit',
    props: {
      commit: Object
    },
    mounted() {
      // console.log(this.commit)
    }
  }
</script>
