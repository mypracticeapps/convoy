(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["about"],{"0636":function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement;t._self._c;return t._m(0)},i=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"about"},[r("h1",[t._v("Ops some error occurred")])])}],o=r("2877"),s={},u=Object(o["a"])(s,n,i,!1,null,null,null);e["default"]=u.exports},"0a49":function(t,e,r){var n=r("9b43"),i=r("626a"),o=r("4bf8"),s=r("9def"),u=r("cd1c");t.exports=function(t,e){var r=1==t,c=2==t,a=3==t,l=4==t,p=6==t,h=5==t||p,f=e||u;return function(e,u,d){for(var v,b,m=o(e),y=i(m),S=n(u,d,3),_=s(y.length),E=0,w=r?f(e,_):c?f(e,0):void 0;_>E;E++)if((h||E in y)&&(v=y[E],b=S(v,E,m),t))if(r)w[E]=b;else if(b)switch(t){case 3:return!0;case 5:return v;case 6:return E;case 2:w.push(v)}else if(l)return!1;return p?-1:a||l?l:w}}},1169:function(t,e,r){var n=r("2d95");t.exports=Array.isArray||function(t){return"Array"==n(t)}},"2f21":function(t,e,r){"use strict";var n=r("79e5");t.exports=function(t,e){return!!t&&n(function(){e?t.call(null,function(){},1):t.call(null)})}},"2fdb":function(t,e,r){"use strict";var n=r("5ca1"),i=r("d2c8"),o="includes";n(n.P+n.F*r("5147")(o),"String",{includes:function(t){return!!~i(this,t,o).indexOf(t,arguments.length>1?arguments[1]:void 0)}})},"3c3c":function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"repos-new"},[r("div",{staticClass:"repos-new-nav"},[r("form",[r("div",{staticClass:"input-field search-wrap"},[r("input",{directives:[{name:"stream",rawName:"v-stream:keyup",value:t.autocomplete$,expression:"autocomplete$",arg:"keyup"}],staticClass:"form-control",attrs:{id:"search",type:"text",placeholder:"search repository"}})]),r("div",{staticClass:"input-field sort-field-wrap"},[r("div",{staticClass:"dropdown"},[r("button",{staticClass:"btn btn-light dropdown-toggle",attrs:{type:"button","data-toggle":"dropdown"}},[t._v("\n              "+t._s(t.filterState.sortBy)+"\n            ")]),r("div",{staticClass:"dropdown-menu",attrs:{"aria-labelledby":"dropdownMenuButton"}},[r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.uiSetSortBy("NAME")}}},[t._v("name")]),r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.uiSetSortBy("BRANCHES")}}},[t._v("branches")]),r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.uiSetSortBy("COMMITS")}}},[t._v("commits")]),r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.uiSetSortBy("DISK_SIZE")}}},[t._v("disk size")]),r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.uiSetSortBy("REFRESH")}}},[t._v("refresh")])])])]),r("div",{staticClass:"input-field sort-direction-wrap"},[r("button",{staticClass:"btn btn-labeled btn-light btn-block",attrs:{type:"button"},on:{click:function(e){return t.uiToggleSortOrder()}}},[r("span",{staticClass:"btn-label"},[r("i",{staticClass:"fas fa-arrow-up",class:{"fa-rotate-180":"ASCENDING"!==t.filterState.sortOrder}})])])])]),t._l(t.repoStore.repos,function(e){return r("Repotitle",{directives:[{name:"show",rawName:"v-show",value:!e.uiHide,expression:"!repo.uiHide"}],key:e.id,class:{selected:Boolean(t.selectedRepo)&&t.selectedRepo.id===e.id},attrs:{repo:e},nativeOn:{click:function(r){return t.uiSetSelectedRepoIndex(e)}}})})],2),r("div",{staticClass:"repos-new-content-container"},[t.uiIsVisible("LOADING_REPO")?r("div",{staticClass:"repos-new-content center"},[r("h2",[t._v("loading repos")])]):t._e(),t.uiIsVisible("LOADING_REPO_FAILED")?r("div",{staticClass:"repos-new-content center"},[r("h2",[t._v("repos load failed")])]):t._e(),t.uiIsVisible("UPDATE_REPO_FAILED")?r("div",{staticClass:"repos-new-content center"},[r("h2",[t._v("repos update failed")])]):t._e(),t.uiIsVisible("REPO_NOT_SELECTED")?r("div",{staticClass:"repos-new-content center"},[r("h2",[t._v("please select a repository")])]):t._e(),r("repo",{directives:[{name:"show",rawName:"v-show",value:t.uiIsVisible("SHOW_REPO"),expression:"uiIsVisible('SHOW_REPO')"}],attrs:{repo$:t.repoObservable$}})],1)])},i=[],o=(r("4f37"),function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"repos-new-content center"},[t.repo&&t.uiIsVisible("REPO_QUEUED_FOR_INDEX")?r("div",{staticClass:"repos-new-content-wrapper center"},[r("h2",[t._v("Repository queued for reindex "+t._s(t.repo.name))])]):t._e(),t.repo&&t.uiIsVisible("REPO_BEING_INDEXED")?r("div",{staticClass:"repos-new-content-wrapper center"},[r("h2",[t._v("Repository being indexed "+t._s(t.repo.name))])]):t._e(),t.repo&&t.uiIsVisible("REPO_INDEX_ERROR")?r("div",{staticClass:"repos-new-content-wrapper center"},[r("h2",[t._v("Unable to index Repository "+t._s(t.repo.name))])]):t._e(),t.repo&&t.uiIsVisible("SHOW_REPO")?r("div",{staticClass:"repos-new-content-wrapper"},[r("form",{staticClass:"app-toolbar justify-content-start mb-5"},[r("div",{staticClass:"dropdown"},[r("button",{staticClass:"btn btn-light dropdown-toggle",attrs:{type:"button","data-toggle":"dropdown"}},[t._v("\n          REFRESH REPO\n        ")]),r("div",{staticClass:"dropdown-menu",attrs:{"aria-labelledby":"dropdownMenuButton"}},[r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(e){return t.refreshGit()}}},[t._v("REFRESH REPO")])])]),r("div",{staticClass:"dropdown"},[r("button",{staticClass:"btn btn-light dropdown-toggle",attrs:{type:"button","data-toggle":"dropdown"}},[t._v("\n          "+t._s(t.selectedBranchName)+"\n        ")]),r("div",{staticClass:"dropdown-menu",attrs:{"aria-labelledby":"dropdownMenuButton"}},t._l(t.repo.branches,function(e){return r("a",{staticClass:"dropdown-item",attrs:{href:"#"},on:{click:function(r){return t.uiSetSelectedBranchName(e.name)}}},[t._v(t._s(e.name))])}),0)])]),r("div",{staticClass:"wrapper"},[t._l(t.commits,function(t){return r("Commit",{attrs:{commit:t}})}),r("button",{directives:[{name:"show",rawName:"v-show",value:this.commits.length>0,expression:"this.commits.length>0"}],staticClass:"btn btn-block btn-secondary btn-lg mb-5 mt-5",attrs:{disabled:!t.uiHasMoreCommits()},on:{click:function(e){return t.fetchNextSetOfCommits()}}},[t._v("load more\n      ")])],2)]):t._e()])}),s=[],u=r("75fc"),c=(r("7f7f"),r("2bd2")),a=r("9ab4");function l(t){return t&&"function"===typeof t.schedule}var p=r("8ac6"),h=r("1453"),f=function(t){function e(){return null!==t&&t.apply(this,arguments)||this}return a["a"](e,t),e.prototype.notifyNext=function(t,e,r,n,i){this.destination.next(e)},e.prototype.notifyError=function(t,e){this.destination.error(t)},e.prototype.notifyComplete=function(t){this.destination.complete()},e}(h["a"]),d=function(t){function e(e,r,n){var i=t.call(this)||this;return i.parent=e,i.outerValue=r,i.outerIndex=n,i.index=0,i}return a["a"](e,t),e.prototype._next=function(t){this.parent.notifyNext(this.outerValue,t,this.outerIndex,this.index++,this)},e.prototype._error=function(t){this.parent.notifyError(t,this),this.unsubscribe()},e.prototype._complete=function(){this.parent.notifyComplete(this),this.unsubscribe()},e}(h["a"]),v=function(t){return function(e){for(var r=0,n=t.length;r<n&&!e.closed;r++)e.next(t[r]);e.complete()}},b=r("0ca4"),m=function(t){return function(e){return t.then(function(t){e.closed||(e.next(t),e.complete())},function(t){return e.error(t)}).then(null,b["a"]),e}};function y(){return"function"===typeof Symbol&&Symbol.iterator?Symbol.iterator:"@@iterator"}var S=y(),_=function(t){return function(e){var r=t[S]();do{var n=r.next();if(n.done){e.complete();break}if(e.next(n.value),e.closed)break}while(1);return"function"===typeof r.return&&e.add(function(){r.return&&r.return()}),e}},E=r("c539"),w=function(t){return function(e){var r=t[E["a"]]();if("function"!==typeof r.subscribe)throw new TypeError("Provided object does not correctly implement Symbol.observable");return r.subscribe(e)}},g=function(t){return t&&"number"===typeof t.length&&"function"!==typeof t};function O(t){return!!t&&"function"!==typeof t.subscribe&&"function"===typeof t.then}var x=r("31c4"),R=function(t){if(t&&"function"===typeof t[E["a"]])return w(t);if(g(t))return v(t);if(O(t))return m(t);if(t&&"function"===typeof t[S])return _(t);var e=Object(x["a"])(t)?"an invalid object":"'"+t+"'",r="You provided "+e+" where a stream was expected. You can provide an Observable, Promise, Array, or Iterable.";throw new TypeError(r)},C=r("e9b9");function T(t,e,r,n,i){if(void 0===i&&(i=new d(t,r,n)),!i.closed)return e instanceof C["a"]?e.subscribe(i):R(e)(i)}var A=r("a6e8");function D(t,e){return new C["a"](function(r){var n=new A["a"],i=0;return n.add(e.schedule(function(){i!==t.length?(r.next(t[i++]),r.closed||n.add(this.schedule())):r.complete()})),n})}function I(t,e){return e?D(t,e):new C["a"](v(t))}var N={};function L(){for(var t=[],e=0;e<arguments.length;e++)t[e]=arguments[e];var r=null,n=null;return l(t[t.length-1])&&(n=t.pop()),"function"===typeof t[t.length-1]&&(r=t.pop()),1===t.length&&Object(p["a"])(t[0])&&(t=t[0]),I(t,n).lift(new P(r))}var P=function(){function t(t){this.resultSelector=t}return t.prototype.call=function(t,e){return e.subscribe(new j(t,this.resultSelector))},t}(),j=function(t){function e(e,r){var n=t.call(this,e)||this;return n.resultSelector=r,n.active=0,n.values=[],n.observables=[],n}return a["a"](e,t),e.prototype._next=function(t){this.values.push(N),this.observables.push(t)},e.prototype._complete=function(){var t=this.observables,e=t.length;if(0===e)this.destination.complete();else{this.active=e,this.toRespond=e;for(var r=0;r<e;r++){var n=t[r];this.add(T(this,n,n,r))}}},e.prototype.notifyComplete=function(t){0===(this.active-=1)&&this.destination.complete()},e.prototype.notifyNext=function(t,e,r,n,i){var o=this.values,s=o[r],u=this.toRespond?s===N?--this.toRespond:this.toRespond:0;o[r]=e,0===u&&(this.resultSelector?this._tryResultSelector(o):this.destination.next(o.slice()))},e.prototype._tryResultSelector=function(t){var e;try{e=this.resultSelector.apply(this,t)}catch(r){return void this.destination.error(r)}this.destination.next(e)},e}(f),B=r("bc3a"),H=r.n(B);function k(t,e,r){var n="repoId="+t.id+"&";e="branchName="+e+"&",r="commitId="+r+"&";var i="size=25",o="/api/v1/commits?"+n+e+r+i;return H.a.get(o).then(function(t){return t.data})}var M={getCommits:k},F=M;r("55dd"),r("6762"),r("2fdb"),r("ac6a"),r("f3e2");function G(){for(var t=[],e=0;e<arguments.length;e++)t[e]=arguments[e];var r=t[t.length-1];return l(r)?(t.pop(),D(t,r)):I(t)}var V=function(t){function e(e,r){return t.call(this)||this}return a["a"](e,t),e.prototype.schedule=function(t,e){return void 0===e&&(e=0),this},e}(A["a"]),q=function(t){function e(e,r){var n=t.call(this,e,r)||this;return n.scheduler=e,n.work=r,n.pending=!1,n}return a["a"](e,t),e.prototype.schedule=function(t,e){if(void 0===e&&(e=0),this.closed)return this;this.state=t;var r=this.id,n=this.scheduler;return null!=r&&(this.id=this.recycleAsyncId(n,r,e)),this.pending=!0,this.delay=e,this.id=this.id||this.requestAsyncId(n,this.id,e),this},e.prototype.requestAsyncId=function(t,e,r){return void 0===r&&(r=0),setInterval(t.flush.bind(t,this),r)},e.prototype.recycleAsyncId=function(t,e,r){if(void 0===r&&(r=0),null!==r&&this.delay===r&&!1===this.pending)return e;clearInterval(e)},e.prototype.execute=function(t,e){if(this.closed)return new Error("executing a cancelled action");this.pending=!1;var r=this._execute(t,e);if(r)return r;!1===this.pending&&null!=this.id&&(this.id=this.recycleAsyncId(this.scheduler,this.id,null))},e.prototype._execute=function(t,e){var r=!1,n=void 0;try{this.work(t)}catch(i){r=!0,n=!!i&&i||new Error(i)}if(r)return this.unsubscribe(),n},e.prototype._unsubscribe=function(){var t=this.id,e=this.scheduler,r=e.actions,n=r.indexOf(this);this.work=null,this.state=null,this.pending=!1,this.scheduler=null,-1!==n&&r.splice(n,1),null!=t&&(this.id=this.recycleAsyncId(e,t,null)),this.delay=null},e}(V),$=function(){function t(e,r){void 0===r&&(r=t.now),this.SchedulerAction=e,this.now=r}return t.prototype.schedule=function(t,e,r){return void 0===e&&(e=0),new this.SchedulerAction(this,t).schedule(r,e)},t.now=function(){return Date.now()},t}(),U=function(t){function e(r,n){void 0===n&&(n=$.now);var i=t.call(this,r,function(){return e.delegate&&e.delegate!==i?e.delegate.now():n()})||this;return i.actions=[],i.active=!1,i.scheduled=void 0,i}return a["a"](e,t),e.prototype.schedule=function(r,n,i){return void 0===n&&(n=0),e.delegate&&e.delegate!==this?e.delegate.schedule(r,n,i):t.prototype.schedule.call(this,r,n,i)},e.prototype.flush=function(t){var e=this.actions;if(this.active)e.push(t);else{var r;this.active=!0;do{if(r=t.execute(t.state,t.delay))break}while(t=e.shift());if(this.active=!1,r){while(t=e.shift())t.unsubscribe();throw r}}},e}($),X=new U(q);function W(t){return!Object(p["a"])(t)&&t-parseFloat(t)+1>=0}function J(t,e){return void 0===t&&(t=0),void 0===e&&(e=X),(!W(t)||t<0)&&(t=0),e&&"function"===typeof e.schedule||(e=X),new C["a"](function(r){return r.add(e.schedule(Y,t,{subscriber:r,counter:0,period:t})),r})}function Y(t){var e=t.subscriber,r=t.counter,n=t.period;e.next(r),this.schedule({subscriber:e,counter:r+1,period:n},n)}var K=r("53c9"),Q=r("ebb6");function z(){if(K["a"].XMLHttpRequest)return new K["a"].XMLHttpRequest;if(K["a"].XDomainRequest)return new K["a"].XDomainRequest;throw new Error("CORS is not supported by your browser")}function Z(){if(K["a"].XMLHttpRequest)return new K["a"].XMLHttpRequest;var t=void 0;try{for(var e=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"],r=0;r<3;r++)try{if(t=e[r],new K["a"].ActiveXObject(t))break}catch(n){}return new K["a"].ActiveXObject(t)}catch(n){throw new Error("XMLHttpRequest is not supported by your browser")}}function tt(t,e){return void 0===e&&(e=null),new ut({method:"GET",url:t,headers:e})}function et(t,e,r){return new ut({method:"POST",url:t,body:e,headers:r})}function rt(t,e){return new ut({method:"DELETE",url:t,headers:e})}function nt(t,e,r){return new ut({method:"PUT",url:t,body:e,headers:r})}function it(t,e,r){return new ut({method:"PATCH",url:t,body:e,headers:r})}var ot=Object(Q["a"])(function(t,e){return t.response});function st(t,e){return ot(new ut({method:"GET",url:t,responseType:"json",headers:e}))}var ut=function(t){function e(e){var r=t.call(this)||this,n={async:!0,createXHR:function(){return this.crossDomain?z():Z()},crossDomain:!0,withCredentials:!1,headers:{},method:"GET",responseType:"json",timeout:0};if("string"===typeof e)n.url=e;else for(var i in e)e.hasOwnProperty(i)&&(n[i]=e[i]);return r.request=n,r}return a["a"](e,t),e.prototype._subscribe=function(t){return new ct(t,this.request)},e.create=function(){var t=function(t){return new e(t)};return t.get=tt,t.post=et,t.delete=rt,t.put=nt,t.patch=it,t.getJSON=st,t}(),e}(C["a"]),ct=function(t){function e(e,r){var n=t.call(this,e)||this;n.request=r,n.done=!1;var i=r.headers=r.headers||{};r.crossDomain||n.getHeader(i,"X-Requested-With")||(i["X-Requested-With"]="XMLHttpRequest");var o=n.getHeader(i,"Content-Type");return o||K["a"].FormData&&r.body instanceof K["a"].FormData||"undefined"===typeof r.body||(i["Content-Type"]="application/x-www-form-urlencoded; charset=UTF-8"),r.body=n.serializeBody(r.body,n.getHeader(r.headers,"Content-Type")),n.send(),n}return a["a"](e,t),e.prototype.next=function(t){this.done=!0;var e,r=this,n=r.xhr,i=r.request,o=r.destination;try{e=new at(t,n,i)}catch(s){return o.error(s)}o.next(e)},e.prototype.send=function(){var t=this,e=t.request,r=t.request,n=r.user,i=r.method,o=r.url,s=r.async,u=r.password,c=r.headers,a=r.body;try{var l=this.xhr=e.createXHR();this.setupEvents(l,e),n?l.open(i,o,s,n,u):l.open(i,o,s),s&&(l.timeout=e.timeout,l.responseType=e.responseType),"withCredentials"in l&&(l.withCredentials=!!e.withCredentials),this.setHeaders(l,c),a?l.send(a):l.send()}catch(p){this.error(p)}},e.prototype.serializeBody=function(t,e){if(!t||"string"===typeof t)return t;if(K["a"].FormData&&t instanceof K["a"].FormData)return t;if(e){var r=e.indexOf(";");-1!==r&&(e=e.substring(0,r))}switch(e){case"application/x-www-form-urlencoded":return Object.keys(t).map(function(e){return encodeURIComponent(e)+"="+encodeURIComponent(t[e])}).join("&");case"application/json":return JSON.stringify(t);default:return t}},e.prototype.setHeaders=function(t,e){for(var r in e)e.hasOwnProperty(r)&&t.setRequestHeader(r,e[r])},e.prototype.getHeader=function(t,e){for(var r in t)if(r.toLowerCase()===e.toLowerCase())return t[r]},e.prototype.setupEvents=function(t,e){var r=e.progressSubscriber;function n(t){var e,r=n,i=r.subscriber,o=r.progressSubscriber,s=r.request;o&&o.error(t);try{e=new vt(this,s)}catch(u){e=u}i.error(e)}if(t.ontimeout=n,n.request=e,n.subscriber=this,n.progressSubscriber=r,t.upload&&"withCredentials"in t){var i,o;if(r)i=function(t){var e=i.progressSubscriber;e.next(t)},K["a"].XDomainRequest?t.onprogress=i:t.upload.onprogress=i,i.progressSubscriber=r;o=function(t){var e,r=o,n=r.progressSubscriber,i=r.subscriber,s=r.request;n&&n.error(t);try{e=new pt("ajax error",this,s)}catch(u){e=u}i.error(e)},t.onerror=o,o.request=e,o.subscriber=this,o.progressSubscriber=r}function s(t){}function u(t){var e=u,r=e.subscriber,n=e.progressSubscriber,i=e.request;if(4===this.readyState){var o=1223===this.status?204:this.status,s="text"===this.responseType?this.response||this.responseText:this.response;if(0===o&&(o=s?200:0),o<400)n&&n.complete(),r.next(t),r.complete();else{n&&n.error(t);var c=void 0;try{c=new pt("ajax error "+o,this,i)}catch(a){c=a}r.error(c)}}}t.onreadystatechange=s,s.subscriber=this,s.progressSubscriber=r,s.request=e,t.onload=u,u.subscriber=this,u.progressSubscriber=r,u.request=e},e.prototype.unsubscribe=function(){var e=this,r=e.done,n=e.xhr;!r&&n&&4!==n.readyState&&"function"===typeof n.abort&&n.abort(),t.prototype.unsubscribe.call(this)},e}(h["a"]),at=function(){function t(t,e,r){this.originalEvent=t,this.xhr=e,this.request=r,this.status=e.status,this.responseType=e.responseType||r.responseType,this.response=ft(this.responseType,e)}return t}();function lt(t,e,r){return Error.call(this),this.message=t,this.name="AjaxError",this.xhr=e,this.request=r,this.status=e.status,this.responseType=e.responseType||r.responseType,this.response=ft(this.responseType,e),this}lt.prototype=Object.create(Error.prototype);var pt=lt;function ht(t){return"response"in t?t.responseType?t.response:JSON.parse(t.response||t.responseText||"null"):JSON.parse(t.responseText||"null")}function ft(t,e){switch(t){case"json":return ht(e);case"xml":return e.responseXML;case"text":default:return"response"in e?e.response:e.responseText}}function dt(t,e){return pt.call(this,"ajax timeout",t,e),this.name="AjaxTimeoutError",this}var vt=dt,bt=ut.create;function mt(t,e){return new C["a"](function(r){var n=new A["a"];return n.add(e.schedule(function(){var i=t[E["a"]]();n.add(i.subscribe({next:function(t){n.add(e.schedule(function(){return r.next(t)}))},error:function(t){n.add(e.schedule(function(){return r.error(t)}))},complete:function(){n.add(e.schedule(function(){return r.complete()}))}}))})),n})}function yt(t,e){return new C["a"](function(r){var n=new A["a"];return n.add(e.schedule(function(){return t.then(function(t){n.add(e.schedule(function(){r.next(t),n.add(e.schedule(function(){return r.complete()}))}))},function(t){n.add(e.schedule(function(){return r.error(t)}))})})),n})}function St(t,e){if(!t)throw new Error("Iterable cannot be null");return new C["a"](function(r){var n,i=new A["a"];return i.add(function(){n&&"function"===typeof n.return&&n.return()}),i.add(e.schedule(function(){n=t[S](),i.add(e.schedule(function(){if(!r.closed){var t,e;try{var i=n.next();t=i.value,e=i.done}catch(o){return void r.error(o)}e?r.complete():(r.next(t),this.schedule())}}))})),i})}function _t(t){return t&&"function"===typeof t[E["a"]]}function Et(t){return t&&"function"===typeof t[S]}function wt(t,e){if(null!=t){if(_t(t))return mt(t,e);if(O(t))return yt(t,e);if(g(t))return D(t,e);if(Et(t)||"string"===typeof t)return St(t,e)}throw new TypeError((null!==t&&typeof t||t)+" is not observable")}function gt(t,e){return e?wt(t,e):t instanceof C["a"]?t:new C["a"](R(t))}function Ot(t,e,r){return void 0===r&&(r=Number.POSITIVE_INFINITY),"function"===typeof e?function(n){return n.pipe(Ot(function(r,n){return gt(t(r,n)).pipe(Object(Q["a"])(function(t,i){return e(r,t,n,i)}))},r))}:("number"===typeof e&&(r=e),function(e){return e.lift(new xt(t,r))})}var xt=function(){function t(t,e){void 0===e&&(e=Number.POSITIVE_INFINITY),this.project=t,this.concurrent=e}return t.prototype.call=function(t,e){return e.subscribe(new Rt(t,this.project,this.concurrent))},t}(),Rt=function(t){function e(e,r,n){void 0===n&&(n=Number.POSITIVE_INFINITY);var i=t.call(this,e)||this;return i.project=r,i.concurrent=n,i.hasCompleted=!1,i.buffer=[],i.active=0,i.index=0,i}return a["a"](e,t),e.prototype._next=function(t){this.active<this.concurrent?this._tryNext(t):this.buffer.push(t)},e.prototype._tryNext=function(t){var e,r=this.index++;try{e=this.project(t,r)}catch(n){return void this.destination.error(n)}this.active++,this._innerSub(e,t,r)},e.prototype._innerSub=function(t,e,r){var n=new d(this,void 0,void 0),i=this.destination;i.add(n),T(this,t,e,r,n)},e.prototype._complete=function(){this.hasCompleted=!0,0===this.active&&0===this.buffer.length&&this.destination.complete(),this.unsubscribe()},e.prototype.notifyNext=function(t,e,r,n,i){this.destination.next(e)},e.prototype.notifyComplete=function(t){var e=this.buffer;this.remove(t),this.active--,e.length>0?this._next(e.shift()):0===this.active&&this.hasCompleted&&this.destination.complete()},e}(f);function Ct(t){return t}function Tt(t){return void 0===t&&(t=Number.POSITIVE_INFINITY),Ot(Ct,t)}function At(){return Tt(1)}function Dt(){for(var t=[],e=0;e<arguments.length;e++)t[e]=arguments[e];return At()(G.apply(void 0,t))}function It(){for(var t=[],e=0;e<arguments.length;e++)t[e]=arguments[e];var r=t[t.length-1];return l(r)?(t.pop(),function(e){return Dt(t,e,r)}):function(e){return Dt(t,e)}}var Nt=r("4b59");function Lt(t){return function(e){var r=new Pt(t),n=e.lift(r);return r.caught=n}}var Pt=function(){function t(t){this.selector=t}return t.prototype.call=function(t,e){return e.subscribe(new jt(t,this.selector,this.caught))},t}(),jt=function(t){function e(e,r,n){var i=t.call(this,e)||this;return i.selector=r,i.caught=n,i}return a["a"](e,t),e.prototype.error=function(e){if(!this.isStopped){var r=void 0;try{r=this.selector(e,this.caught)}catch(i){return void t.prototype.error.call(this,i)}this._unsubscribeAndRecycle();var n=new d(this,void 0,void 0);this.add(n),T(this,r,void 0,void 0,n)}},e}(f);function Bt(t,e){return"function"===typeof e?function(r){return r.pipe(Bt(function(r,n){return gt(t(r,n)).pipe(Object(Q["a"])(function(t,i){return e(r,t,n,i)}))}))}:function(e){return e.lift(new Ht(t))}}var Ht=function(){function t(t){this.project=t}return t.prototype.call=function(t,e){return e.subscribe(new kt(t,this.project))},t}(),kt=function(t){function e(e,r){var n=t.call(this,e)||this;return n.project=r,n.index=0,n}return a["a"](e,t),e.prototype._next=function(t){var e,r=this.index++;try{e=this.project(t,r)}catch(error){return void this.destination.error(error)}this._innerSub(e,t,r)},e.prototype._innerSub=function(t,e,r){var n=this.innerSubscription;n&&n.unsubscribe();var i=new d(this,void 0,void 0),o=this.destination;o.add(i),this.innerSubscription=T(this,t,e,r,i)},e.prototype._complete=function(){var e=this.innerSubscription;e&&!e.closed||t.prototype._complete.call(this),this.unsubscribe()},e.prototype._unsubscribe=function(){this.innerSubscription=null},e.prototype.notifyComplete=function(e){var r=this.destination;r.remove(e),this.innerSubscription=null,this.isStopped&&t.prototype._complete.call(this)},e.prototype.notifyNext=function(t,e,r,n,i){this.destination.next(e)},e}(f),Mt={REPO_LOAD_STATE:"LOADING",repos:[],response:{}},Ft={sortBy:"COMMITS",sortOrder:"ASCENDING",searchTerm:""},Gt=new c["a"],Vt=Gt.asObservable().pipe(It(Ft),Object(Nt["a"])()),qt="/api/v1/repos",$t=function(t){return Mt.repos=t.response.data,Mt.response=t,Mt.REPO_LOAD_STATE="LOADED",Object.assign({},Mt)},Ut=function(t){return Mt.response=t,"LOADED"===Mt.REPO_LOAD_STATE||"UPDATE_FAILED"===Mt.REPO_LOAD_STATE?Mt.REPO_LOAD_STATE="UPDATE_FAILED":"LOADING"!==Mt.REPO_LOAD_STATE&&"FAILED"!==Mt.REPO_LOAD_STATE||(Mt.REPO_LOAD_STATE="FAILED"),G(Object.assign({},Mt))},Xt=function(t){var e=t[1],r=t[0];return e.repos.forEach(function(t){return t.uiHide=!1}),e.repos.forEach(function(t){return t.uiHide=!t.name.includes(r.searchTerm)}),[r,e]},Wt=function(t,e,r){return t=t.sort(function(t,n){var i=e(t),o=e(n);return"ASCENDING"===r.toUpperCase()?i>o?1:i<o?-1:0:i<o?1:i>o?-1:0}),t},Jt=function(t){var e=t[1],r=t[0],n="";"NAME"===r.sortBy&&(n=function(t){return t.name}),"BRANCHES"===r.sortBy&&(n=function(t){return t.branches.length}),"COMMITS"===r.sortBy&&(n=function(t){return t.totalCommits}),"DISK_SIZE"===r.sortBy&&(n=function(t){return t.diskUsage}),"REFRESH"===r.sortBy&&(n=function(t){return t.status.lastRefreshedAt});var i=Wt(e.repos,n,r.sortOrder);return e.repos=i,[r,e]},Yt=function(t){return t[1]},Kt=bt(qt).pipe(Object(Q["a"])($t),Lt(Ut)),Qt=J(2e3).pipe(It(0)),zt=Qt.pipe(Bt(function(){return Kt}),It(Mt),Object(Nt["a"])()),Zt=L(Vt.pipe(),zt).pipe(Object(Q["a"])(Xt),Object(Q["a"])(Jt),Object(Q["a"])(Yt)),te={repos$:Zt,setSortBy:function(t){Ft.sortBy=t.toUpperCase(),Gt.next(Ft)},setSortOrder:function(t){Ft.sortOrder=t.toUpperCase(),Gt.next(Ft)},setSearchTerm:function(t){Ft.searchTerm=t,Gt.next(Ft)},refreshGit:function(t){var e="repoId="+t.id,r="/api/v1/repos/actions/refresh/git?"+e;return H.a.get(r).then(function(t){return t.data})}},ee=te,re=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"card commit"},[r("div",{staticClass:"card-body commit-body"},[r("div",{staticClass:"commit-info"},[r("p",{staticClass:"font-weight-bold text-left mb-1"},[t._v(t._s(t.commit.message))]),r("p",{staticClass:"font-weight-normal text-left author-info"},[t._v("\n        committed on "+t._s(t.commit.committer.time.substring(0,10))+" by "+t._s(t.commit.committer.name)+"\n      ")])]),r("div",{staticClass:"commit-meta-data"},[r("p",{staticClass:"font-weight-normal text-right"},[t._v(t._s(t.commit.id.substring(0,7)))])])])])},ne=[],ie={name:"Commit",props:{commit:Object},mounted:function(){}},oe=ie,se=r("2877"),ue=Object(se["a"])(oe,re,ne,!1,null,null,null),ce=ue.exports;function ae(t,e){return function(r){return r.lift(new le(t,e))}}var le=function(){function t(t,e){this.predicate=t,this.thisArg=e}return t.prototype.call=function(t,e){return e.subscribe(new pe(t,this.predicate,this.thisArg))},t}(),pe=function(t){function e(e,r,n){var i=t.call(this,e)||this;return i.predicate=r,i.thisArg=n,i.count=0,i}return a["a"](e,t),e.prototype._next=function(t){var e;try{e=this.predicate.call(this.thisArg,t,this.count++)}catch(r){return void this.destination.error(r)}e&&this.destination.next(t)},e}(h["a"]);function he(t,e){return function(r){return r.lift(new fe(t,e))}}var fe=function(){function t(t,e){this.compare=t,this.keySelector=e}return t.prototype.call=function(t,e){return e.subscribe(new de(t,this.compare,this.keySelector))},t}(),de=function(t){function e(e,r,n){var i=t.call(this,e)||this;return i.keySelector=n,i.hasKey=!1,"function"===typeof r&&(i.compare=r),i}return a["a"](e,t),e.prototype.compare=function(t,e){return t===e},e.prototype._next=function(t){var e;try{var r=this.keySelector;e=r?r(t):t}catch(o){return this.destination.error(o)}var n=!1;if(this.hasKey)try{var i=this.compare;n=i(this.key,e)}catch(o){return this.destination.error(o)}else this.hasKey=!0;n||(this.key=e,this.destination.next(t))},e}(h["a"]),ve={name:"repo",props:{repo$:Object},components:{Commit:ce},subscriptions:function(){this.branchName$=new c["a"]},data:function(){return{repo:{name:""},selectedBranchName:"master",commits:[]}},methods:{uiIsVisible:function(t){return!!this.repo.name&&("REPO_QUEUED_FOR_INDEX"===t?"QUEUED"===this.repo.status.progress:"REPO_BEING_INDEXED"===t?"IN_PROGRESS"===this.repo.status.progress:"SHOW_REPO"===t?"DONE"===this.repo.status.progress:"REPO_INDEX_ERROR"===t?"ERROR"===this.repo.status.progress:void 0)},uiSetSelectedBranchName:function(t){this.selectedBranchName=t,this.fetchFirstSetOfCommits()},uiHasMoreCommits:function(){return 0!=this.commits.length&&Boolean(this.commits[this.commits.length-1].sortOrderNext)},retrieveCommits:function(t,e,r){var n=this;F.getCommits(t,e,r).then(function(t){var e;(e=n.commits).push.apply(e,Object(u["a"])(t.data))},function(t){console.log(t)})},fetchNextSetOfCommits:function(){if(""!==this.repo.name){var t=this.commits[this.commits.length-1];t.sortOrderNext&&this.retrieveCommits(this.repo,this.selectedBranch,t.sortOrderNext)}},fetchFirstSetOfCommits:function(){if(""!==this.repo.name&&"DONE"===this.repo.status.progress){this.commits=[];var t=this.getBranchFromRepo(this.repo,this.selectedBranchName);this.retrieveCommits(this.repo,this.selectedBranchName,t.latestCommitId)}},getBranchFromRepo:function(t,e){for(var r=t.branches,n=r.length,i=0;i<n;i++){var o=r[i];if(o.name===e)return o}},refreshGit:function(){ee.refreshGit(this.repo)}},mounted:function(){var t=this,e=this.repo$.pipe(ae(Boolean),he(function(t,e){return t.version===e.version})),r=this.branchName$.asObservable().pipe(It("master"),he());L(e,r).subscribe(function(e){t.repo=e[0],t.selectedBranchName=e[1],t.fetchFirstSetOfCommits()})}},be=ve,me=Object(se["a"])(be,o,s,!1,null,null,null),ye=me.exports,Se=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"repo-title",class:t.st},[r("h6",{staticClass:"text-left"},[t._v(t._s(t.repo.name))]),r("p",{staticClass:"text-left mb-0"},[t._v(t._s(t.repo.id))]),r("div",{staticClass:"stats"},[r("span",[r("i",{staticClass:"fas fa-code-branch"}),t._v(" "+t._s(t.repo.branches.length))]),r("span",[r("i",{staticClass:"far fa-code-commit fa-rotate-90"}),t._v(" "+t._s(t.repo.totalCommits))]),r("span",[r("i",{staticClass:"far fa-database"}),t._v(" "+t._s(t.repo.diskUsage)+" MB")]),r("span",[r("i",{staticClass:"far fa-sync"}),t._v(" "+t._s(t.uiTimestampToDateStr(t.repo.status.lastRefreshedAt)))])])])},_e=[],Ee=(r("a00e"),{name:"Repotitle",props:{repo:Object},computed:{st:function(){var t={};return t.selected=void 0!==this.selectedRepo&&this.selectedRepo.id===this.repo.id,t.queued="QUEUED"===this.repo.status.progress,t.progressrp="IN_PROGRESS"===this.repo.status.progress,t.error="ERROR"===this.repo.status.progress,t.done="DONE"===this.repo.status.progress,t}},methods:{uiTimestampToDateStr:function(t){var e=new Date(t);return e.toLocaleDateString("en-US")},uiGetRepoClass:function(t){var e={};return e.selected=void 0!==this.selectedRepo&&this.selectedRepo.id===t.id,e.queued="QUEUED"===t.status.progress,e.progressrp="IN_PROGRESS"===t.status.progress,e.error="ERROR"===t.status.progress,e.done="DONE"===t.status.progress,e}},mounted:function(){}}),we=Ee,ge=Object(se["a"])(we,Se,_e,!1,null,null,null),Oe=ge.exports,xe=r("f20f"),Re=function(t){function e(e){var r=t.call(this)||this;return r._value=e,r}return a["a"](e,t),Object.defineProperty(e.prototype,"value",{get:function(){return this.getValue()},enumerable:!0,configurable:!0}),e.prototype._subscribe=function(e){var r=t.prototype._subscribe.call(this,e);return r&&!r.closed&&e.next(this._value),r},e.prototype.getValue=function(){if(this.hasError)throw this.thrownError;if(this.closed)throw new xe["a"];return this._value},e.prototype.next=function(e){t.prototype.next.call(this,this._value=e)},e}(c["a"]);function Ce(t,e){return void 0===e&&(e=X),function(r){return r.lift(new Te(t,e))}}var Te=function(){function t(t,e){this.dueTime=t,this.scheduler=e}return t.prototype.call=function(t,e){return e.subscribe(new Ae(t,this.dueTime,this.scheduler))},t}(),Ae=function(t){function e(e,r,n){var i=t.call(this,e)||this;return i.dueTime=r,i.scheduler=n,i.debouncedSubscription=null,i.lastValue=null,i.hasValue=!1,i}return a["a"](e,t),e.prototype._next=function(t){this.clearDebounce(),this.lastValue=t,this.hasValue=!0,this.add(this.debouncedSubscription=this.scheduler.schedule(De,this.dueTime,this))},e.prototype._complete=function(){this.debouncedNext(),this.destination.complete()},e.prototype.debouncedNext=function(){if(this.clearDebounce(),this.hasValue){var t=this.lastValue;this.lastValue=null,this.hasValue=!1,this.destination.next(t)}},e.prototype.clearDebounce=function(){var t=this.debouncedSubscription;null!==t&&(this.remove(t),t.unsubscribe(),this.debouncedSubscription=null)},e}(h["a"]);function De(t){t.debouncedNext()}var Ie={subscriptions:function(){this.autocomplete$=new c["a"],this.repoSubject$=new Re,this.repoObservable$=this.repoSubject$.asObservable().pipe(Object(Nt["a"])())},components:{repo:ye,Repotitle:Oe},computed:{},data:function(){return{repoStore:{REPO_LOAD_STATE:"LOADING",repos:[],response:{}},filterState:{sortBy:"NAME",sortOrder:"ASCENDING",searchTerm:""},selectedRepo:void 0}},methods:{uiIsVisible:function(t){if("LOADING_REPO"===t)return"LOADING"===this.repoStore.REPO_LOAD_STATE;if("LOADING_REPO_FAILED"===t)return"FAILED"===this.repoStore.REPO_LOAD_STATE;if("UPDATE_REPO_FAILED"===t)return"UPDATE_FAILED"===this.repoStore.REPO_LOAD_STATE;if("REPO_NOT_SELECTED"===t){var e="LOADED"===this.repoStore.REPO_LOAD_STATE;return e&&!Boolean(this.selectedRepo)}if("SHOW_REPO"===t){var r="LOADED"===this.repoStore.REPO_LOAD_STATE&&Boolean(this.selectedRepo);return r}},uiSetSelectedRepoIndex:function(t){this.selectedRepo=t,this.repoSubject$.next(this.selectedRepo)},uiSetSortBy:function(t){this.filterState.sortBy=t,ee.setSortBy(t)},uiToggleSortOrder:function(){"ASCENDING"===this.filterState.sortOrder?this.filterState.sortOrder="DESCENDING":this.filterState.sortOrder="ASCENDING",ee.setSortOrder(this.filterState.sortOrder)}},mounted:function(){var t=this;this.$subscribeTo(ee.repos$,function(e){t.repoStore=e}),this.autocomplete$.pipe(Ce(200),Object(Q["a"])(function(t){return t.event.target.value}),Object(Q["a"])(function(t){return t.trim()}),he()).subscribe(function(t){ee.setSearchTerm(t)})}},Ne=Ie,Le=Object(se["a"])(Ne,n,i,!1,null,null,null);e["default"]=Le.exports},"4f37":function(t,e,r){"use strict";r("aa77")("trim",function(t){return function(){return t(this,3)}})},5147:function(t,e,r){var n=r("2b4c")("match");t.exports=function(t){var e=/./;try{"/./"[t](e)}catch(r){try{return e[n]=!1,!"/./"[t](e)}catch(i){}}return!0}},"53c9":function(t,e,r){"use strict";(function(t){r.d(e,"a",function(){return s});var n="undefined"!==typeof window&&window,i="undefined"!==typeof self&&"undefined"!==typeof WorkerGlobalScope&&self instanceof WorkerGlobalScope&&self,o="undefined"!==typeof t&&t,s=n||o||i}).call(this,r("c8ba"))},"55dd":function(t,e,r){"use strict";var n=r("5ca1"),i=r("d8e8"),o=r("4bf8"),s=r("79e5"),u=[].sort,c=[1,2,3];n(n.P+n.F*(s(function(){c.sort(void 0)})||!s(function(){c.sort(null)})||!r("2f21")(u)),"Array",{sort:function(t){return void 0===t?u.call(o(this)):u.call(o(this),i(t))}})},6762:function(t,e,r){"use strict";var n=r("5ca1"),i=r("c366")(!0);n(n.P,"Array",{includes:function(t){return i(this,t,arguments.length>1?arguments[1]:void 0)}}),r("9c6c")("includes")},"9cd4":function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement;t._self._c;return t._m(0)},i=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"about"},[r("h1",[t._v("Server is starting up. Please wait")])])}],o=(r("d92a"),r("bc3a")),s=r.n(o),u={ping:function(){return s.a.get("/api/v1/ping")}},c=u,a={methods:{pingServer:function(){var t=this;c.ping().then(function(){console.log("Starter started"),t.$router.push({name:"home"})},function(e){421===error.response.status?(console.log("Server not started. retrying in one second"),setTimeout(function(){this.pingServer()}.bind(t),1e3)):(console.log("server failed with some other error"),t.$router.push("error"))})}},mounted:function(){this.pingServer()}},l=a,p=r("2877"),h=Object(p["a"])(l,n,i,!1,null,null,null);e["default"]=h.exports},a00e:function(t,e,r){},aa77:function(t,e,r){var n=r("5ca1"),i=r("be13"),o=r("79e5"),s=r("fdef"),u="["+s+"]",c="​",a=RegExp("^"+u+u+"*"),l=RegExp(u+u+"*$"),p=function(t,e,r){var i={},u=o(function(){return!!s[t]()||c[t]()!=c}),a=i[t]=u?e(h):s[t];r&&(i[r]=a),n(n.P+n.F*u,"String",i)},h=p.trim=function(t,e){return t=String(i(t)),1&e&&(t=t.replace(a,"")),2&e&&(t=t.replace(l,"")),t};t.exports=p},aae3:function(t,e,r){var n=r("d3f4"),i=r("2d95"),o=r("2b4c")("match");t.exports=function(t){var e;return n(t)&&(void 0!==(e=t[o])?!!e:"RegExp"==i(t))}},ac6a:function(t,e,r){for(var n=r("cadf"),i=r("0d58"),o=r("2aba"),s=r("7726"),u=r("32e9"),c=r("84f2"),a=r("2b4c"),l=a("iterator"),p=a("toStringTag"),h=c.Array,f={CSSRuleList:!0,CSSStyleDeclaration:!1,CSSValueList:!1,ClientRectList:!1,DOMRectList:!1,DOMStringList:!1,DOMTokenList:!0,DataTransferItemList:!1,FileList:!1,HTMLAllCollection:!1,HTMLCollection:!1,HTMLFormElement:!1,HTMLSelectElement:!1,MediaList:!0,MimeTypeArray:!1,NamedNodeMap:!1,NodeList:!0,PaintRequestList:!1,Plugin:!1,PluginArray:!1,SVGLengthList:!1,SVGNumberList:!1,SVGPathSegList:!1,SVGPointList:!1,SVGStringList:!1,SVGTransformList:!1,SourceBufferList:!1,StyleSheetList:!0,TextTrackCueList:!1,TextTrackList:!1,TouchList:!1},d=i(f),v=0;v<d.length;v++){var b,m=d[v],y=f[m],S=s[m],_=S&&S.prototype;if(_&&(_[l]||u(_,l,h),_[p]||u(_,p,m),c[m]=h,y))for(b in n)_[b]||o(_,b,n[b],!0)}},cd1c:function(t,e,r){var n=r("e853");t.exports=function(t,e){return new(n(t))(e)}},d2c8:function(t,e,r){var n=r("aae3"),i=r("be13");t.exports=function(t,e,r){if(n(e))throw TypeError("String#"+r+" doesn't accept regex!");return String(i(t))}},e853:function(t,e,r){var n=r("d3f4"),i=r("1169"),o=r("2b4c")("species");t.exports=function(t){var e;return i(t)&&(e=t.constructor,"function"!=typeof e||e!==Array&&!i(e.prototype)||(e=void 0),n(e)&&(e=e[o],null===e&&(e=void 0))),void 0===e?Array:e}},f3e2:function(t,e,r){"use strict";var n=r("5ca1"),i=r("0a49")(0),o=r("2f21")([].forEach,!0);n(n.P+n.F*!o,"Array",{forEach:function(t){return i(this,t,arguments[1])}})},f820:function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement;t._self._c;return t._m(0)},i=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"about"},[r("h1",[t._v("This is an about page")])])}],o=r("2877"),s={},u=Object(o["a"])(s,n,i,!1,null,null,null);e["default"]=u.exports},fdef:function(t,e){t.exports="\t\n\v\f\r   ᠎             　\u2028\u2029\ufeff"}}]);
//# sourceMappingURL=about.7feba601.js.map