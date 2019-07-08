<template>
  <div class="repo-container">
    <div class="repo-nav">
      <h3 v-if="isReposLoading">Loading repos</h3>

      <div class="repo-title" v-for="(repo, index) in repos" :key="repo.id"
           @click="setSelectedRepo(index)" :class="{selected: selectedRepo == index}">
        <h5 class="text-left">{{repo.name}}</h5>
        <h6 class="text-left">{{repo.id}}</h6>
      </div>
    </div>
    <div class="repo-body">
      <div class="toolbar">
        <div class="btn-group pull-left">
          <button type="button" class="btn btn-danger">master</button>
          <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <span class="sr-only">Toggle Dropdown</span>
          </button>
          <div class="dropdown-menu" >
            <a class="dropdown-item" v-for="branch in branches" href="#" @click="setSelectedBranch(branch.name)">{{branch.name}}</a>
          </div>
        </div>
        <div class="btn-group">
          <button type="button" class="btn btn-danger">refresh ui</button>
          <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <span class="sr-only">Toggle Dropdown</span>
          </button>
          <div class="dropdown-menu">
            <a class="dropdown-item" href="#">refresh git</a>
            <a class="dropdown-item" href="#">refresh stories</a>
          </div>
        </div>
      </div>

      <div class="card commit" v-for="commit in commits" :key="commit.id">
        <div class="card-body commit-body">
          <div class="commit-info">
            <p class="font-weight-bold text-left mb-1">{{commit.message}}</p>
            <p class="font-weight-normal text-left author-info">
              commited on {{commit.committer.time.substring(0,10)}} by {{commit.committer.name}}
            </p>
          </div>
          <div class="commit-meta-data">
            <p class="font-weight-normal text-right">{{commit.id.substring(0,7)}}</p>
            <p class="font-weight-normal text-right">US123456</p>
          </div>
        </div>
      </div>

      <div class="footer text-center">
        <button class="btn btn-small btn-primary" :disabled="isLoadMoreBtnDisabled" @click="loadMoreCommits()">load more</button>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import RepoAPI from "@/services/repos.api";

export default {
  name: "home",
  data() {
    return {
      isReposLoading: true,
      isCommitsLoading: true,
      isLoadMoreBtnDisabled: false,
      repos: [],
      commits: [],
      selectedRepo: -1,
      selectedBranch: 'master'
    };
  },
  computed:  {
    branches () {
      if(this.selectedRepo == -1) return [];
      let repo = this.repos[this.selectedRepo];
      return repo.branches;
    }
  },
  methods: {
    setSelectedRepo(index){
      this.selectedRepo = index;
      this.selectedBranch = "master";
      this.isLoadMoreBtnDisabled = false;
      this.fetchCommits();
    },
    setSelectedBranch(branch){
      this.selectedBranch = branch;
      this.isLoadMoreBtnDisabled = false;
      this.fetchCommits();
    },
    fetchCommits(){
      if(this.selectedRepo == -1) return;
      let repo = this.repos[this.selectedRepo];
      RepoAPI.getCommits(repo, "branch", this.selectedBranch).then(commits => this.commits=commits);
    },
    loadMoreCommits(){
      if(this.selectedRepo == -1) return;
      let repo = this.repos[this.selectedRepo];
      let lastCommitId = this.commits[this.commits.length - 1].id;
      if(lastCommitId){
          RepoAPI
            .getCommits(repo, "commit", lastCommitId)
            .then(commits => {
                console.log(commits);
                if(commits.length == 0){
                  this.isLoadMoreBtnDisabled = true; // no more commits disable button
                }
                this.commits.push(...commits);
              });
      }
    }
  },
  mounted() {
    RepoAPI.getRepos().then(repos=>{
      this.isReposLoading = false;
      this.repos = repos;
      if(this.repos.length > 0){
        this.setSelectedRepo(0);
      }
    });
  }
};
</script>