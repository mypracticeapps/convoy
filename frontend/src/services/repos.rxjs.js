import {Subject, interval, of, combineLatest} from 'rxjs';
import {ajax} from 'rxjs/ajax';
import {switchMap, pluck, catchError, startWith, map, share, tap, withLatestFrom} from 'rxjs/operators';
import axios from "axios";

const store = {
  REPO_LOAD_STATE: 'LOADING', // LOADING, LOADED, FAILED, UPDATE_FAILED
  repos: [],
  response: {}
};

const filter = {
  sortBy: 'COMMITS',
  sortOrder: 'ASCENDING', // ASCENDING
  searchTerm: ''
};

const filterSubject$ = new Subject();
const filter$ = filterSubject$.asObservable().pipe(startWith(filter), share());
const getAllReposUrl = process.env.VUE_APP_ROOT_API + `/repos`;

const mapAjaxSuccessResponse = (response) => {
  store.repos = response.response.data;
  store.response = response;
  store.REPO_LOAD_STATE = "LOADED";
  return Object.assign({}, store);
};

const mapAjaxFailureResponse = (response) => {
  store.response = response;
  if (store.REPO_LOAD_STATE === 'LOADED' || store.REPO_LOAD_STATE === 'UPDATE_FAILED') {
    store.REPO_LOAD_STATE = "UPDATE_FAILED";
  } else if (store.REPO_LOAD_STATE === 'LOADING' || store.REPO_LOAD_STATE === 'FAILED') {
    store.REPO_LOAD_STATE = "FAILED";
  }
  return of(Object.assign({}, store));
};

const filterReposFromStream = (val) => {
  let repoStore = val[1];
  let terms = val[0];
  repoStore.repos.forEach((repo) => repo.uiHide = false);
  repoStore.repos.forEach((repo) => repo.uiHide = repo.name.includes(terms.searchTerm) ? false : true);
  // repoStore.repos = repos;
  return [terms, repoStore];
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

const sortReposFromStream = (val) => {
  let repoStore = val[1];
  let terms = val[0];
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
  if (terms.sortBy === 'REFRESH') {
    callback = (repo) => repo.status.lastRefreshedAt;
  }
  let repos = genericSort(repoStore.repos, callback, terms.sortOrder);
  repoStore.repos = repos;
  return [terms, repoStore];
};


const plunkRepos = (val) => {
  return val[1];
};

const reposAjax$ = ajax(getAllReposUrl)
  .pipe(
    map(mapAjaxSuccessResponse),
    catchError(mapAjaxFailureResponse),
  );

const interval$ = interval(2000).pipe(startWith(0));

const repos$ = interval$.pipe(
  switchMap(() => reposAjax$),
  startWith(store),
  share()
);

let filteredRepos = combineLatest(filter$.pipe(), repos$)
  .pipe(map(filterReposFromStream), map(sortReposFromStream), map(plunkRepos));
filteredRepos = filteredRepos.pipe(tap(val=>console.log(process.env)));

let RepoAPI = {
  repos$: filteredRepos,
  setSortBy(val) {
    filter.sortBy = val.toUpperCase();
    filterSubject$.next(filter);
  },
  setSortOrder(val) {
    filter.sortOrder = val.toUpperCase();
    filterSubject$.next(filter);
  },
  setSearchTerm(val) {
    filter.searchTerm = val;
    filterSubject$.next(filter);
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
