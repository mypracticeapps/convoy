<template>
  <div class="repos-new-content center">
    <div class="repos-new-content-wrapper center" v-if="repo && uiIsVisible('REPO_QUEUED_FOR_INDEX')">
      <h2>Repository queued for reindex {{repo.name}}</h2>
    </div>

    <div class="repos-new-content-wrapper center" v-if="repo && uiIsVisible('REPO_BEING_INDEXED')">
      <h2>Repository being indexed {{repo.name}}</h2>
    </div>

    <div class="repos-new-content-wrapper center flex-column" v-if="repo && uiIsVisible('REPO_INDEX_ERROR')">
      <h2>Unable to index Repository {{repo.name}}</h2>
      <button class="btn btn-secondary mt-3" @click="refreshGit()">try again</button>
    </div>

    <div class="repos-new-content-wrapper" v-if="repo && uiIsVisible('SHOW_REPO')">
      <form class="app-toolbar justify-content-start mb-5">
        <div class="dropdown">
          <button class="btn btn-light dropdown-toggle" type="button" data-toggle="dropdown">
            REFRESH REPO
          </button>
          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a class="dropdown-item" href="#" @click="refreshGit()">REFRESH REPO</a>
          </div>
        </div>
        <div class="dropdown">
          <button class="btn btn-light dropdown-toggle" type="button" data-toggle="dropdown">
            {{selectedBranchName}}
          </button>
          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a class="dropdown-item" href="#" @click="uiSetSelectedBranchName(branch.name)"
               v-for="branch in repo.branches">{{branch.name}}</a>
          </div>
        </div>
      </form>
      <div class="wrapper">
        <Commit v-for="commit in commits" :key="commit.id" :commit="commit"></Commit>
        <button class="btn btn-block btn-secondary btn-lg mb-5 mt-5"
                :disabled="!uiHasMoreCommits()"
                @click="fetchNextSetOfCommits()"
                v-show="this.commits.length>0">load more
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import {Subject, BehaviorSubject, interval, of, combineLatest} from 'rxjs';
  import CommitAPI from "@/services/commits.rxjs";
  import RepoAPI from "@/services/repos.rxjs";
  import Commit from '@/components/Commit.vue'

  import {
    distinctUntilChanged,
    debounceTime,
    map,
    tap,
    share,
    filter,
    startWith
  } from 'rxjs/operators';

  export default {
    name: 'repo',
    props: {
      repo$: Object
    },
    components: {Commit},
    subscriptions() {
      this.branchName$ = new Subject()
    },
    data() {
      return {
        sm: new RepoStateMachine(this),
        repo: {
          name: ''
        },
        selectedBranchName: 'master',
        commits: []
      }
    },
    methods: {
      uiIsVisible(key) {
        if (!this.repo.name) {
          return false;
        }
        if (key === 'REPO_QUEUED_FOR_INDEX') {
          return this.repo.status.progress === 'QUEUED';
        }
        if (key === 'REPO_BEING_INDEXED') {
          return this.repo.status.progress === 'IN_PROGRESS';
        }
        if (key === 'SHOW_REPO') {
          return this.repo.status.progress === 'DONE';
        }
        if (key === 'REPO_INDEX_ERROR') {
          return this.repo.status.progress === 'ERROR';
        }
      },
      uiSetSelectedBranchName(value) {
        this.selectedBranchName = value;
        this.fetchFirstSetOfCommits();
      },
      uiHasMoreCommits() {
        if (this.commits.length == 0) return false;
        return Boolean(this.commits[this.commits.length - 1].sortOrderNext);
      },
      retrieveCommits(repo, branchName, fromCommitId) {
        CommitAPI.getCommits(repo, branchName, fromCommitId)
          .subscribe(response => {
            // console.log(response.data.data);
            this.commits.push(...response.data.data);
          }, errors => {
            console.log(error);
          });
      },
      fetchNextSetOfCommits() {
        if (this.repo.name === '') return;
        let lastCommit = this.commits[this.commits.length - 1];
        if (lastCommit.sortOrderNext) {
          this.retrieveCommits(this.repo, this.selectedBranch, lastCommit.sortOrderNext);
        }
      },
      fetchFirstSetOfCommits() {
        if (this.repo.name === '') return;
        if (this.repo.status.progress !== 'DONE') return;
        this.commits = [];
        let branch = this.getBranchFromRepo(this.repo, this.selectedBranchName);
        this.retrieveCommits(this.repo, this.selectedBranchName, branch.latestCommitId);
      },
      getBranchFromRepo(repo, branchName) {
        var branches = repo.branches;
        var arrayLength = branches.length;
        for (var ii = 0; ii < arrayLength; ii++) {
          let branch = branches[ii];
          if (branch.name === branchName)
            return branch;
        }
      },
      refreshGit() {
        RepoAPI.refreshGit(this.repo);
      }
    },
    mounted() {
      let repo$ = this.repo$
        .pipe(
          filter(Boolean),
        );
      let branchName$ = this.branchName$.asObservable().pipe(startWith('master'), distinctUntilChanged());
      combineLatest(repo$, branchName$).subscribe((val) => {
        this.repo = val[0];
        this.selectedBranchName = val[1];
        this.fetchFirstSetOfCommits();
      });
    }
  }


  class RepoStateMachine {
    constructor(self) {
      this.self = self;
      this.data = {
        firstLoad: true
      };
      this.currStates = new Set();
      this.compState = {
        CMP_NO_REPO: false,
        CMP_INDEXING: false,
        CMP_QUEUED: true,
        CMP_INDEX_ERROR: false,
        CMP_LOADING_COMMITS: false,
        CMP_LOADING_COMMITS_FAILED: false,
        CMP_SHOW_COMMITS: false,
      };
      this.preDefinedStates = [
        "REPO_CHANGED",
        "LOADING_COMMITS",
        "LOADED_COMMITS",
        "LOAD_FAILED_COMMITS",
        "INDEXED",
        "INDEX_FAILED",
        "QUEUED",
        "INDEXING"
      ];
    }

    visible(uiComponent) {
      if (!this.compState.hasOwnProperty(uiComponent)) {
        throw "UI Component NOT supported: " + uiComponent;
      }
      return this.compState[uiComponent];
    }

    show(uiComponent) {
      this.visible(uiComponent);
    }

    state(uiState) {
      if (!this.preDefinedStates.includes(uiState)) throw "UNKNOWN UI STATE : " + uiState;
      // this.preDefinedStates = ["REPO_CHANGED", "LOADING_COMMITS", "LOADED_COMMITS", "LOAD_FAILED_COMMITS",
      //     "INDEXED", "INDEX_FAILED", "QUEUED", "INDEXING"];
      if (uiState === 'REPO_CHANGED') {
        this.data.firstLoad = true;
        this.currStates.delete("INDEXED");
        this.currStates.delete("INDEX_FAILED");
        this.currStates.delete("QUEUED");
        this.currStates.delete("INDEXING");
        if (this.self.repo.name) {
          if (this.self.repo.status.progress === "QUEUED") {
            this.currStates.add("QUEUED");
          } else if (this.self.repo.status.progress === "IN_PROGRESS") {
            this.currStates.add("INDEXING");
          } else if (this.self.repo.status.progress === "DONE") {
            this.currStates.add("INDEXED");
          } else if (this.self.repo.status.progress === "ERROR") {
            this.currStates.add("INDEX_FAILED");
          }
        }
      } else if (uiState === 'LOADING_COMMITS') {
        this.currStates.add(uiState);
        this.currStates.delete("LOADED_COMMITS");
        this.currStates.delete("LOAD_FAILED_COMMITS");
      } else if (uiState === 'LOADED_COMMITS') {
        this.currStates.add(uiState);
        this.currStates.delete("LOADING_COMMITS");
        this.currStates.delete("LOAD_ERROR");
        this.data.firstLoad = false;
      } else if (uiState === 'LOAD_FAILED_COMMITS') {
        this.currStates.add(uiState);
        this.currStates.delete("LOADING_COMMITS");
        this.currStates.delete('LOADED_COMMITS');
      }
      this._calc();
    }

    _calc() {
      // this.preDefinedStates = ["REPO_CHANGED", "LOADING_COMMITS", "LOADED_COMMITS", "LOAD_FAILED_COMMITS",
      //     "INDEXED", "INDEX_FAILED", "QUEUED", "INDEXING"];

      // repository undefined
      // loading first set of commits
      // loading next set of commits
      // error loading commits and retry
      // TODO
    }

    _set(st) {
      this.compState = st;
      // console.log(JSON.stringify(st));
    }
  }
</script>
