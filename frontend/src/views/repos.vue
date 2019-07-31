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
              {{sortBy}}
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <a class="dropdown-item" href="#" @click="uiSetSortBy('NAME')">name</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('BRANCHES')">branches</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('COMMITS')">commits</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('DISK_SIZE')">disk size</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('REFRESH')">refresh</a>
              <a class="dropdown-item" href="#" @click="uiSetSortBy('PROGRESS')">progress</a>
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
      <div class="repos-new-content center" v-if="sm.visible('CMP_LOADING')">
        <h2>loading repos</h2>
      </div>
      <div class="repos-new-content center repo-load-failed" v-if="sm.visible('CMP_LOAD_FAILED')">
        <h2>repos load failed</h2>
      </div>
      <div class="repos-new-content center repo-update-failed" v-if="sm.visible('CMP_UPDATE_FAILED')">
        <h2>repos update failed</h2>
      </div>
      <div class="repos-new-content center repo-select" v-if="sm.visible('CMP_REPO_SELECT_NOTIFY')">
        <h2>please select a repository</h2>
      </div>
      <repo v-show="sm.visible('CMP_SHOW_REPO')" :repo$="repoObservable$" class="test"></repo>
    </div>
  </div>
</template>

<script>
    import repo from "@/components/repo.vue";
    import Repotitle from "./repotitle/Repotitle.vue";
    import RepoAPI from "@/services/repos.rxjs";
    import {BehaviorSubject, Subject} from 'rxjs';
    import {debounceTime, distinctUntilChanged, map, share} from 'rxjs/operators';

    export default {
        subscriptions() {
            this.autocomplete$ = new Subject(),
                this.repoSubject$ = new BehaviorSubject(),
                this.repoObservable$ = this.repoSubject$.asObservable().pipe(share())
        },
        components: {
            repo, Repotitle
        },
        computed: {
            sortBy() {
                if (!this.repoStore.filter) return '';
                if (!this.repoStore.filter.sortBy) return '';
                return this.repoStore.filter.sortBy;
            }
        },
        data() {
            return {
                sm: new StateMachine(this),
                repoStore: {
                    REPO_LOAD_STATE: 'LOADING', // LOADING, LOADED, FAILED, UPDATE_FAILED
                    repos: [],
                    response: {},
                    filter: {
                        sortBy: ''
                    }
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
                    if (this.repoStore.REPO_LOAD_STATE !== 'LOADED') return false;
                    if (!this.selectedRepo) return true;
                    if (this.selectedRepo.uiHide) return true;
                }
                if (key === 'SHOW_REPO') {
                    if (this.repoStore.REPO_LOAD_STATE !== 'LOADED') return false;
                    if (!this.selectedRepo) return false;
                    if (!this.selectedRepo.uiHide) return true;
                }
            },
            uiSetSelectedRepoIndex(repo) {
                this.selectedRepo = repo;
                this.sm.state("REPO_SELECTED");
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
            // this.preDefinedStates = ["REPO_SELECTED", "REPO_NOT_SELECTED", "LOAD_ERROR", "LOADING", "LOAD_COMPLETE"];
            // REPO_LOAD_STATE: 'LOADING', // LOADING, LOADED, FAILED, UPDATE_FAILED

            this.sm.state("LOADING");
            this.sm.state("REPO_NOT_SELECTED");
            this.repoStoreSubscrition$ = this.$subscribeTo(RepoAPI.repos$, (val) => {
                this.repoStore = val;
                if (this.repoStore.REPO_LOAD_STATE === 'REPO_API_FAILED') {
                    this.sm.state('LOAD_ERROR');
                }
                if (this.repoStore.REPO_LOAD_STATE === 'REPO_API_LOADED') {
                    this.sm.state('LOAD_COMPLETE');
                }

                let getUpdatedRepo = (repoStore) => {
                    if (!this.selectedRepo) return undefined;
                    let updatedRepo = repoStore.repos.find(r => r.id === this.selectedRepo.id);
                    return updatedRepo;
                };

                let updatedRepo = getUpdatedRepo(this.repoStore);

                let isUpdatedRepoVisible = (next) => {
                    if (!next) return false;
                    return !next.uiHide;
                };
                if (isUpdatedRepoVisible(updatedRepo)) {
                    this.sm.state("REPO_SELECTED")
                } else {
                    this.sm.state("REPO_NOT_SELECTED")
                }

                let isRepoChanged = (oldRepo, newRepo) => {
                    if(!oldRepo && !newRepo) return false;
                    if (oldRepo && !newRepo || !oldRepo && newRepo) {
                        return true;
                    }
                    let lastRefreshChanged = oldRepo.status.lastRefreshedAt !== newRepo.status.lastRefreshedAt;
                    let progressChanged = oldRepo.status.progress !== newRepo.status.progress;
                    return lastRefreshChanged || progressChanged;
                };

                if(isRepoChanged(this.selectedRepo, updatedRepo)){
                    this.selectedRepo = updatedRepo;
                    this.repoSubject$.next(this.selectedRepo);
                } else {
                    // TODO check use case
                    this.selectedRepo = updatedRepo;
                }
            });
            this.autocomplete$.pipe(
                debounceTime(200),
                map(event => event.event.target.value),
                map(value => value.trim()),
                distinctUntilChanged(),
            ).subscribe(value => {
                RepoAPI.setSearchTerm(value);
            });
        },
        beforeDestroy() {
            this.repoStoreSubscrition$.unsubscribe();
        }
    };

    class StateMachine {
        constructor(self) {
            this.self = self;
            this.data = {
                firstLoad: true
            };
            this.compState = {
                CMP_SIDE_NAV: false,
                CMP_LOADING: true,
                CMP_UPDATE_FAILED: false,
                CMP_LOAD_FAILED: false,
                CMP_REPO_SELECT_NOTIFY: false,
                CMP_SHOW_REPO: false
            };

            this.currStates = new Set();

            this.preDefinedStates = ["REPO_SELECTED", "REPO_NOT_SELECTED", "LOAD_ERROR", "LOADING", "LOAD_COMPLETE"];
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
            if (!this.preDefinedStates.includes(uiState)) throw "UNKNOWN UI STATE";
            // console.log("NEW STATE: " + uiState);
            if (uiState === 'LOADING') {
                this.currStates.add(uiState);
                // if this is not first load, we can safe to leave all the ui components as it is
            } else if (uiState === 'LOAD_ERROR') {
                this.currStates.add(uiState);
                this.currStates.delete("LOADING");
            } else if (uiState === 'LOAD_COMPLETE') {
                this.currStates.add(uiState);
                this.currStates.delete("LOADING");
                this.data.firstLoad = false;
            } else if (uiState === 'REPO_SELECTED') {
                this.currStates.add(uiState);
                this.currStates.delete('REPO_NOT_SELECTED');
            } else if (uiState === 'REPO_NOT_SELECTED') {
                this.currStates.add(uiState);
                this.currStates.delete('REPO_SELECTED');
            }
            this._calc();
        }

        _calc() {
            // this.preDefinedStates = ["REPO_SELECTED", "REPO_NOT_SELECTED", "LOAD_ERROR", "LOADING", "LOAD_COMPLETE"]; for easy reference
            // initial loading
            // console.log(this.currStates);
            if (this.currStates.has("LOADING") && this.data.firstLoad) {
                this._set({
                    CMP_SIDE_NAV: false,
                    CMP_LOADING: true,
                    CMP_UPDATE_FAILED: false,
                    CMP_LOAD_FAILED: false,
                    CMP_REPO_SELECT_NOTIFY: false,
                    CMP_SHOW_REPO: false
                });
            } else

            // initial load failed
            if (this.currStates.has("LOAD_ERROR" && this.data.firstLoad)) {
                this._set({
                    CMP_SIDE_NAV: false,
                    CMP_LOADING: false,
                    CMP_UPDATE_FAILED: false,
                    CMP_LOAD_FAILED: true,
                    CMP_REPO_SELECT_NOTIFY: false,
                    CMP_SHOW_REPO: false
                });
            } else

            // initial load success and subsequent load failed
            if (this.currStates.has("LOAD_ERROR" && !this.data.firstLoad)) {
                this._set({
                    CMP_SIDE_NAV: true,
                    CMP_LOADING: false,
                    CMP_UPDATE_FAILED: true,
                    CMP_LOAD_FAILED: false,
                    CMP_REPO_SELECT_NOTIFY: false,
                    CMP_SHOW_REPO: false
                });
            } else

            // repos loaded but not selected
            if (this.currStates.has("LOAD_COMPLETE") && this.currStates.has("REPO_NOT_SELECTED")) {
                this._set({
                    CMP_SIDE_NAV: true,
                    CMP_LOADING: false,
                    CMP_UPDATE_FAILED: false,
                    CMP_LOAD_FAILED: false,
                    CMP_REPO_SELECT_NOTIFY: true,
                    CMP_SHOW_REPO: false
                });
            } else

            // repos loaded and selected
            if (this.currStates.has("LOAD_COMPLETE") && this.currStates.has("REPO_SELECTED")) {
                this._set({
                    CMP_SIDE_NAV: true,
                    CMP_LOADING: false,
                    CMP_UPDATE_FAILED: false,
                    CMP_LOAD_FAILED: false,
                    CMP_REPO_SELECT_NOTIFY: false,
                    CMP_SHOW_REPO: true
                });
            } else {
                console.log("CALC NO CONDITION FOUND");
            }
        }

        _set(st) {
            this.compState = st;
            // console.log(JSON.stringify(st));
        }
    }
</script>
`
