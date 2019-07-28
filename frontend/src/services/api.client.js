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

class rxios {
  constructor(progressBar, options = {}) {
    this.options = options;
    this.progressBar = progressBar;
  }

  configure(router) {
    this.router = router;

    this._httpClient = axios.create(this.options);

    if (this.progressBar) {
      this._httpClient.interceptors.request.use(config => {
        NProgress.start();
        return config
      });

      this._httpClient.interceptors.response.use(response => {
        NProgress.done();
        return response
      }, error => {
        NProgress.done();
        return error;
      });
    }

    if (this.router) {
      this._httpClient.interceptors.response.use(response => {
        return response
      }, error => {
        console.log(error)
        let status = error.response.status;
        let currentPage = this.router.history.current.name;
        console.log(status, currentPage)
        if (status === 421 && currentPage !== 'startup') {
          this.router.push({name: 'startup'});
        } else {

        }
        return error
      });
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

export const http = new rxios(false, {});
export const httpl = new rxios(true, {});
export default {http, httpl};
