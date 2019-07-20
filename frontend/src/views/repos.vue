<template>
  <div class="repos-new">
    <div class="repos-new-nav">
      <form>
        <div class="input-field search-wrap">
          <input id="search" type="text" placeholder="search repository" class="form-control"
                 v-stream:keyup="autocomplete$"/>
        </div>
        <div class="input-field sort-field-wrap">
          <div class="dropdown">
            <button class="btn btn-light dropdown-toggle" type="button" data-toggle="dropdown">
              {{filterState.sortBy}}
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <a class="dropdown-item" href="#" @click="uiSetSortBy('NAME')">name</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('BRANCHES')">branches</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('COMMITS')">commits</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('DISK_SIZE')">disk size</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('REFRESH')">refresh</a>
            </div>
          </div>
        </div>
        <div class="input-field sort-direction-wrap">
          <button type="button" class="btn btn-labeled btn-light btn-block" @click="uiToggleSortOrder()">
            <span class="btn-label">
              <i class="fas fa-arrow-up" :class="{'fa-rotate-180': filterState.sortOrder !== 'ASCENDING'}"></i>
              </span>
          </button>
        </div>
      </form>
<!--      TODO some of the properties can be moved to child. found: when moved component update is slow-->
      <Repotitle
        v-show="!repo.uiHide"
        v-for="repo in repoStore.repos" :key="repo.id"
        :class="{selected: Boolean(selectedRepo) && selectedRepo.id === repo.id}"
        @click.native="uiSetSelectedRepoIndex(repo)"
      :repo="repo"></Repotitle>
    </div>
    <div class="repos-new-content-container">
      <div class="repos-new-content center" v-if="uiIsVisible('LOADING_REPO')">
        <h2>loading repos</h2>
      </div>
      <div class="repos-new-content center" v-if="uiIsVisible('LOADING_REPO_FAILED')">
        <h2>repos load failed</h2>
      </div>
      <div class="repos-new-content center" v-if="uiIsVisible('UPDATE_REPO_FAILED')">
        <h2>repos update failed</h2>
      </div>
      <div class="repos-new-content center" v-if="uiIsVisible('REPO_NOT_SELECTED')">
        <h2>please select a repository</h2>
      </div>
      <repo v-show="uiIsVisible('SHOW_REPO')" :repo$="repoObservable$"></repo>
    </div>
  </div>
</template>

<script>
  import repo from "@/components/repo.vue";
  import Repotitle from "./repotitle/Repotitle.vue";
  import RepoAPI from "@/services/repos.rxjs";
  import {Subject, BehaviorSubject, interval, of, combineLatest} from 'rxjs';
  import {
    distinctUntilChanged,
    debounceTime,
    map,
    share
  } from 'rxjs/operators';

  export default {
    subscriptions() {
      this.autocomplete$ = new Subject(),
        this.repoSubject$ = new BehaviorSubject(),
        this.repoObservable$ = this.repoSubject$.asObservable().pipe(share())
    },
    components: {
      repo, Repotitle
    },
    computed: {},
    data() {
      return {
        repoStore: {
          REPO_LOAD_STATE: 'LOADING', // LOADING, LOADED, FAILED, UPDATE_FAILED
          repos: [],
          response: {}
        },
        filterState: {
          sortBy: 'NAME',
          sortOrder: 'ASCENDING',
          searchTerm: ''
        },
        selectedRepo: undefined
      };
    },
    methods: {
      uiIsVisible(key) {
        if (key === 'LOADING_REPO') {
          return this.repoStore.REPO_LOAD_STATE === 'LOADING';
        }
        if (key === 'LOADING_REPO_FAILED') {
          return this.repoStore.REPO_LOAD_STATE === 'FAILED';
        }
        if (key === 'UPDATE_REPO_FAILED') {
          return this.repoStore.REPO_LOAD_STATE === 'UPDATE_FAILED';
        }
        if (key === 'REPO_NOT_SELECTED') {
          let preRequests = this.repoStore.REPO_LOAD_STATE === 'LOADED';
          return preRequests && !Boolean(this.selectedRepo);
        }
        if (key === 'SHOW_REPO') {
          let preRequests = this.repoStore.REPO_LOAD_STATE === 'LOADED' && Boolean(this.selectedRepo);
          return preRequests;
        }
      },
      uiSetSelectedRepoIndex(repo) {
        this.selectedRepo = repo;
        this.repoSubject$.next(this.selectedRepo);
      },
      uiSetSortBy(val) {
        this.filterState.sortBy = val;
        RepoAPI.setSortBy(val);
      },
      uiToggleSortOrder() {
        if (this.filterState.sortOrder === 'ASCENDING') {
          this.filterState.sortOrder = 'DESCENDING';
        } else {
          this.filterState.sortOrder = 'ASCENDING';
        }
        RepoAPI.setSortOrder(this.filterState.sortOrder);
      }
    },
    mounted() {
      this.$subscribeTo(RepoAPI.repos$, (val) => {
        this.repoStore = val;
      });
      this.autocomplete$.pipe(
        debounceTime(200),
        map(event => event.event.target.value),
        map(value => value.trim()),
        distinctUntilChanged(),
      ).subscribe(value => {
        RepoAPI.setSearchTerm(value);
      });
    }
  };
</script>
