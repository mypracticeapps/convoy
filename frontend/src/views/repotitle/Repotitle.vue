<template>
  <div class="repo-title" :class="st">
    <h6 class="text-left">{{repo.name}}</h6>
    <p class="text-left mb-0">{{repo.id}}</p>
    <div class="stats">
      <span><i class="fas fa-code-branch"></i> {{repo.branches.length}}</span>
      <span><i class="far fa-code-commit fa-rotate-90"></i> {{repo.totalCommits}}</span>
      <span><i class="far fa-database"></i> {{repo.diskUsage}} MB</span>
      <span><i class="far fa-sync"></i> {{uiTimestampToDateStr(repo.status.lastRefreshedAt)}}</span>
    </div>
  </div>
</template>

<script>
  import './repotitle.scss';
  export default {
    name: "Repotitle",
    props: {
      repo: Object
    },
    computed: {
      st: function(){
        let st = {};
        st.selected = this.selectedRepo !== undefined && this.selectedRepo.id === this.repo.id;
        st.queued = this.repo.status.progress === 'QUEUED';
        st.progressrp = this.repo.status.progress === 'IN_PROGRESS';
        st.error = this.repo.status.progress === 'ERROR';
        st.done = this.repo.status.progress === 'DONE';
        return st;
      }
    },
    methods:{
      uiTimestampToDateStr(timestamp) {
        var date = new Date(timestamp);
        return date.toLocaleDateString("en-US");
      },
      uiGetRepoClass(repo) {
        let st = {};
        st.selected = this.selectedRepo !== undefined && this.selectedRepo.id === repo.id;
        st.queued = repo.status.progress === 'QUEUED';
        st.progressrp = repo.status.progress === 'IN_PROGRESS';
        st.error = repo.status.progress === 'ERROR';
        st.done = repo.status.progress === 'DONE';
        return st;
      },
    },
    mounted() {
    }
  }
</script>
