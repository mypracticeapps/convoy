import axios from 'axios';
import {Observable} from 'rxjs'

axios.interceptors.request.use(config => {
  NProgress.start();
  return config
});

axios.interceptors.response.use(response => {
  NProgress.done();
  return response
}, error => {
  NProgress.done();
  return error;
});


let progressStartIntr = (config) => {
  NProgress.start();
  return config;
};

let progressEndSuccessIntr = (response) => {
  NProgress.done();
  return Promise.resolve(response);
};

let progressEndFailedIntr = (response) => {
  NProgress.done();
  return Promise.reject(response);
};

let startUpIntr = (response) => {
  let status = undefined;
  if(response.response){
    status = response.response.status;
  } else {
    status = response.status;
  }
  if (status === 200) {
    return Promise.resolve(response);
  } else if (status === 421) {
    let currentPage = globalRouter.history.current.name;
    if (currentPage !== 'startup') {
      globalRouter.push({name: 'startup'});
    }
  }
};

let globalRouter = undefined;

class rxios {
  constructor(options = {}, progressBar, startUpIntr) {
    this.options = options;
    this.progressBar = progressBar;
    this.startUpIntr = startUpIntr;
    this.configure();
  }

  configure() {
    this._httpClient = axios.create(this.options);

    if (this.progressBar) {
      this._httpClient.interceptors.request.use(progressStartIntr);
      this._httpClient.interceptors.response.use(progressEndSuccessIntr, progressEndFailedIntr);
    }

    if (this.startUpIntr && globalRouter) {
      this._httpClient.interceptors.response.use(startUpIntr,startUpIntr);
    }
  }

  _makeRequest(method, url, queryParams, body) {
    return new Observable(subscriber => {
      let request;
      switch (method) {
        case 'GET':
          request = this._httpClient.get(url, {params: queryParams});
          break;
        case 'POST':
          request = this._httpClient.post(url, body, {params: queryParams});
          break;
        case 'PUT':
          request = this._httpClient.put(url, body, {params: queryParams});
          break;
        case 'PATCH':
          request = this._httpClient.patch(url, body, {params: queryParams});
          break;
        case 'DELETE':
          request = this._httpClient.delete(url, {params: queryParams});
          break;
        default:
          throw new Error('Method not supported');
      }
      request.then(response => {
        subscriber.next(response);
        subscriber.complete();
      }).catch((err) => {
        subscriber.error(err);
        subscriber.complete();
      });
    });
  }

  get(url, queryParams) {
    return this._makeRequest('GET', url, queryParams);
  }

  post(url, body, queryParams) {
    return this._makeRequest('POST', url, queryParams, body);
  }

  put(url, body, queryParams) {
    return this._makeRequest('PUT', url, queryParams, body);
  }

  patch(url, body, queryParams) {
    return this._makeRequest('PATCH', url, queryParams, body);
  }

  delete(url, queryParams) {
    return this._makeRequest('DELETE', url, queryParams);
  }
}

export const httpConfig = function (router) {
  globalRouter = router;
};

export default rxios;
