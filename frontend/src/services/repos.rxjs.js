import {BehaviorSubject, combineLatest, of} from 'rxjs';
import {catchError, map, switchMap, tap} from 'rxjs/operators';
import rxios from "./api.client"

let http = new rxios({}, false, true);

import axios from 'axios';
import _ from 'lodash';

const REFRESH_INTERVAL = 2000;
const ALL_REPOS_URL = process.env.VUE_APP_ROOT_API + `/repos`;

const filter = {
  sortBy: 'PROGRESS',
  sortOrder: 'ASCENDING', // ASCENDING
  searchTerm: ''
};


const mapSuccess = (response) => {
  let store = {
    repos: response.data.data,
    response: response,
    filter: filter,
    REPO_LOAD_STATE: "REPO_API_LOADED"
  };
  return store;
};

const mapFailed = (response) => {
  let store = {
    repos: [],
    response: response,
    filter: filter,
    REPO_LOAD_STATE: "REPO_API_FAILED"
  };
  return of(store);
};

const filterRepos = (repoStore) => {
  let searchTerm = repoStore.filter.searchTerm;
  repoStore.repos.forEach((repo) => repo.uiHide = true);
  repoStore.repos.forEach((repo) => {
    if (repo.name.includes(searchTerm)) {
      repo.uiHide = false;
    }
  });
  return repoStore;
};

const sortRepos = (repoStore) => {
  let terms = repoStore.filter;
  // console.log(repoStore)
  let callback = "";
  if (terms.sortBy === 'NAME') {
    callback = (repo) => repo.name;
  }
  if (terms.sortBy === 'BRANCHES') {
    callback = (repo) => repo.branches.length;
  }
  if (terms.sortBy === 'COMMITS') {
    callback = (repo) => repo.totalCommits;
  }
  if (terms.sortBy === 'DISK_SIZE') {
    callback = (repo) => repo.diskUsage;
  }
  if (terms.sortBy === 'REFRESH' || terms.sortBy === 'PROGRESS') {
    callback = (repo) => repo.status.lastRefreshedAt;
  }
  let repos = genericSort(repoStore.repos, callback, terms.sortOrder);

  if (terms.sortBy === 'PROGRESS') {
    let inProgress = _.remove(repos, repo => repo.status.progress === "IN_PROGRESS");
    let queued = _.remove(repos, repo => repo.status.progress === "QUEUED");
    let others = repos;
    repos = [];
    repos.push(...inProgress);
    repos.push(...queued);
    repos.push(...others);
  }
  repoStore.repos = repos;
  return repoStore;
};


const genericSort = (arr, callback, orderBy) => {
  arr = arr.sort(function (val1, val2) {
    let first = callback(val1);
    let second = callback(val2);
    if (orderBy.toUpperCase() === "ASCENDING") {
      if (first > second) return 1;
      if (first < second) return -1;
      else return 0;
    } else {
      if (first < second) return 1;
      if (first > second) return -1;
      else return 0;
    }
  });
  return arr;
};

let key$ = new BehaviorSubject(1);
let filter$ = new BehaviorSubject(filter);
let pa$ = http.get(ALL_REPOS_URL).pipe(map(mapSuccess), catchError(mapFailed));

let repos$ = key$.pipe(
  switchMap(() => pa$),
  tap(() => setTimeout(function () {
    key$.next()
  }, REFRESH_INTERVAL))
);
let filteredRepos$ = combineLatest(repos$, filter$, (repos, filters) => {
  repos.filter = filters;
  return repos
})
  .pipe(map(filterRepos), map(sortRepos));


let RepoAPI = {
  repos$: filteredRepos$,
  setSortBy(val) {
    filter.sortBy = val.toUpperCase();
    filter$.next(filter);
  },
  setSortOrder(val) {
    filter.sortOrder = val.toUpperCase();
    filter$.next(filter);
  },
  setSearchTerm(val) {
    filter.searchTerm = val;
    filter$.next(filter);
  },
  refreshGit(repo) {
    let repoId = "repoId=" + repo.id;
    let url = process.env.VUE_APP_ROOT_API + "/repos/actions/refresh/git?" + repoId;
    return axios.get(url).then(response => {
      return response.data
    });
  }
};

export default RepoAPI;



