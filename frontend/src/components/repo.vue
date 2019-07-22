<template>
  <div class="repos-new-content center">
    <div class="repos-new-content-wrapper center" v-if="repo && uiIsVisible('REPO_QUEUED_FOR_INDEX')">
      <h2>Repository queued for reindex {{repo.name}}</h2>
    </div>

    <div class="repos-new-content-wrapper center" v-if="repo && uiIsVisible('REPO_BEING_INDEXED')">
      <h2>Repository being indexed {{repo.name}}</h2>
    </div>

    <div class="repos-new-content-wrapper center" v-if="repo && uiIsVisible('REPO_INDEX_ERROR')">
      <h2>Unable to index Repository {{repo.name}}</h2>
    </div>

    <div class="repos-new-content-wrapper" v-if="repo && uiIsVisible('SHOW_REPO')">
      <form class="app-toolbar justify-content-start mb-5">
        <div class="dropdown">
          <button class="btn btn-light dropdown-toggle" type="button" data-toggle="dropdown">
            <!--              {{filterState.sortBy}}-->
            REFRESH REPO
          </button>
          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a class="dropdown-item" href="#" @click="refreshGit()">REFRESH REPO</a>
            <!--            <a class="dropdown-item" href="#" @click="uiSetSortBy('NAME')">REFRESH STORY</a>-->
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
        <Commit v-for="commit in commits" :commit="commit"></Commit>
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
        CommitAPI.getCommits(repo, branchName, fromCommitId).then(commits => {
          this.commits.push(...commits.data);
        }, (error) => {
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
        if (this.repo.status.progress !== 'DONE') return
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
          distinctUntilChanged((prev, next) => {
            return prev.status.lastRefreshedAt === next.status.lastRefreshedAt;
          }),
        );
      let branchName$ = this.branchName$.asObservable().pipe(startWith('master'), distinctUntilChanged());
      combineLatest(repo$, branchName$).subscribe((val) => {
        // console.log(val)
        this.repo = val[0];
        this.selectedBranchName = val[1];
        this.fetchFirstSetOfCommits();
      });
    }
  }
</script>
