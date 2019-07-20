<template>
  <div class="repo-container">
    <div class="repo-nav" v-if="$getUiState !== 'LOADING_REPOS'">
      <div class="repo-title"
           v-for="(repo, index) in repos" :key="repo.id"
           @click="setSelectedRepo(index)"
           :class="{selected: selectedRepo == index, error: !repo.status.initialized}">
        <h6 class="text-left">{{repo.name}}</h6>
        <p class="text-left mb-0">{{repo.id}}</p>
      </div>
    </div>

    <div class="repo-child-container">
      <div class="repo-body middle-child" v-if="$getUiState === 'LOADING_REPOS'">
        <h2>Repos are loading wait util</h2>
      </div>
      <div class="repo-body middle-child" v-if="$getUiState === 'SELECT_REPO'">
        <h2>please select a repository</h2>
      </div>

      <div class="repo-body middle-child repo-commit-fetch" v-show="$getUiState === 'FETCHING_COMMITS'">
        <h2>fetching commits for <span class="font-weight-bold">{{selectedBranch}}</span></h2>
      </div>

      <div class="repo-body middle-child" v-if="$getUiState === 'REPO_INDEX'">
        <h2>repository is being indexed</h2>
      </div>

      <div class="repo-body middle-child" v-if="$getUiState === 'REPO_ERROR'">
        <div>
          <h2 class="text-center">unable to load repositories</h2>
          <h5 class="text-center">Please refresh and try again</h5>
        </div>
      </div>

      <div class="repo-body middle-child" v-if="$getUiState === 'COMMIT_ERROR'">
        <h2>unable to load commits</h2>
      </div>

      <div class="repo-body" v-if="$getUiState === 'DISPLAY_COMMITS' || $getUiState === 'FETCHING_COMMITS'">
        <div class="toolbar">
          <div class="btn-group">
            <button type="button" class="btn btn-secondary" @click="$refreshUi()">refresh ui</button>
            <button type="button" class="btn btn-secondary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
              <span class="sr-only">Toggle Dropdown</span>
            </button>
            <div class="dropdown-menu">
              <a class="dropdown-item" href="#" @click="$refreshGit()">refresh git</a>
            </div>
          </div>
          <div class="btn-group">
            <button type="button" class="btn btn-secondary">{{selectedBranch}}</button>
            <button type="button" class="btn btn-secondary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
              <span class="sr-only">Toggle Dropdown</span>
            </button>
            <div class="dropdown-menu">
              <a class="dropdown-item" v-for="branch in branches" href="#" @click="setSelectedBranch(branch.name)">{{branch.name}}</a>
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
              <!--              <p class="font-weight-normal text-right">US123456</p>-->
            </div>
          </div>
        </div>

        <div class="footer text-center mt-5 mb-5">
          <button class="btn btn-lg btn-primary load-more-commit-btn" :disabled="toggles.isLoadMoreBtnDisabled"
                  @click="fetchNextSetOfCommits()">load more
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import RepoAPI from "@/services/repos.api";

  export default {
    name: "home",
    data() {
      return {
        toggles: {
          isReposLoading: true,
          isCommitsLoading: false,
          isLoadMoreBtnDisabled: false,
          isRepoBeingIndexed: false,
          isRepoFetchError: false,
          isCommitFetchFailed: false
        },
        repos: [],
        commits: [],
        selectedRepo: -1,
        selectedBranch: ''
      };
    },
    computed: {
      branches() {
        if (this.selectedRepo == -1) return [];
        let repo = this.repos[this.selectedRepo];
        return repo.branches;
      },
      $getUiState() {
        let states = ['LOADING_REPOS', 'SELECT_REPO', 'FETCHING_COMMITS', 'DISPLAY_COMMITS', 'REPO_INDEX', 'REPO_ERROR', 'COMMIT_ERROR'];
        let tg = this.toggles;
        if (tg.isReposLoading)
          return 'LOADING_REPOS';
        if (tg.isRepoFetchError)
          return "REPO_ERROR";
        if (tg.isReposLoading == false && tg.isRepoFetchError == false && this.selectedRepo == -1) {
          return 'SELECT_REPO';
        }
        if (this.selectedRepo != -1 && tg.isRepoBeingIndexed) {
          return 'REPO_INDEX';
        }
        if (this.selectedRepo != -1 && tg.isCommitsLoading) {
          return 'FETCHING_COMMITS';
        }
        if (this.selectedRepo != -1 && tg.isCommitFetchFailed) {
          return 'COMMIT_ERROR';
        }
        if (tg.isCommitsLoading == false) {
          return 'DISPLAY_COMMITS';
        }
      }
    },
    methods: {
      setSelectedRepo(index) {
        if (this.$getUiState === 'REPO_INDEX') return;
        this.selectedRepo = index;
        this.selectedBranch = '';
        this.setSelectedBranch("master");
      },
      setSelectedBranch(branch) {
        if (this.selectedBranch === branch) return;
        this.selectedBranch = branch;
        // todo if selected branch does not contains master then defaults to some branch
        this.isLoadMoreBtnDisabled = false;
        this.commits = [];
        this.fetchNextSetOfCommits();
      },
      retrieveCommits(repo, branchName, fromCommitId) {
        this.toggles.isCommitsLoading = true;
        this.toggles.isCommitFetchFailed = false;
        RepoAPI.getCommits(repo, branchName, fromCommitId).then(commits => {
          this.toggles.isCommitsLoading = false;
          this.toggles.isCommitFetchFailed = false;
          this.toggles.isRepoBeingIndexed = false;

          this.commits.push(...commits.data);
          if (commits.length == 0) {
            this.toggles.isLoadMoreBtnDisabled = true;
          }
        }, (error) => {
          if (!error.response || error.response.status !== 423) {
            this.toggles.isCommitFetchFailed = true;
            this.toggles.isCommitsLoading = false;
            console.log(error);
          } else {
            setTimeout(function () {
              this.toggles.isRepoBeingIndexed = true;
              this.fetchNextSetOfCommits();
            }.bind(this), 1000)
          }
        });
      },
      fetchNextSetOfCommits() {
        if (this.selectedRepo == -1) return;

        let repo = this.repos[this.selectedRepo];
        if (this.commits.length == 0) {
          this.toggles.isLoadMoreBtnDisabled = false;
          let branch = this.getBranchFromRepo(repo, this.selectedBranch);
          this.retrieveCommits(repo, this.selectedBranch, branch.latestCommitId);
        } else {
          let lastCommit = this.commits[this.commits.length - 1];
          if (lastCommit.sortOrderNext) {
            this.retrieveCommits(repo, this.selectedBranch, lastCommit.sortOrderNext);
          } else {
            this.toggles.isLoadMoreBtnDisabled = true;
          }
        }
      },
      updateRepo(repo) {
        this.commits = [];
        RepoAPI.getSingleRepo(repo).then((response) => {
          let repo = response.data;
          this.repos[this.selectedRepo] = repo;
          this.repos.push();
          this.repos.pop();
          this.fetchNextSetOfCommits();
        }, (error) => {
          if (!error.response || error.response.status !== 423) {
            this.toggles.isCommitFetchFailed = true;
            this.toggles.isCommitsLoading = false;
            console.log(error);
          } else {
            setTimeout(function () {
              this.toggles.isRepoBeingIndexed = true;
              this.updateRepo(repo);
            }.bind(this), 1000)
          }
        });
      },
      $refreshUi() {
        if (this.selectedRepo == -1) return;
        let repo = this.repos[this.selectedRepo];
        this.updateRepo(repo);
      },
      $refreshGit() {
        if (this.selectedRepo == -1) return;
        let repo = this.repos[this.selectedRepo];
        this.toggles.isRepoBeingIndexed = true;
        RepoAPI.refreshGit(repo).then(() => {
          console.log("Repo is refreshed: " + repo.id);
          this.updateRepo(repo);
        });
      },
      getBranchFromRepo(repo, branchName) {
        var branches = repo.branches;
        var arrayLength = branches.length;
        for (var ii = 0; ii < arrayLength; ii++) {
          let branch = branches[ii];
          if (branch.name === branchName)
            return branch;
        }
      }
    },
    mounted() {
      this.toggles.isReposLoading = true;
      RepoAPI.getRepos().then(repos => {
        this.toggles.isReposLoading = false;
        this.toggles.isRepoFetchError = false;
        this.repos = repos.data;
        // if (this.repos.length > 0) {
        //   this.setSelectedRepo(0);
        // }
      }, (error) => {
        this.toggles.isReposLoading = false;
        this.toggles.isRepoFetchError = true;
      });
    }
  };
</script>
