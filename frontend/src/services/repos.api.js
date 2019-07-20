import axios from 'axios';

let RepoAPI = {
  getRepos() {
    return axios.get(process.env.VUE_APP_ROOT_API + '/repos').then(response => response.data);
  },

  getSingleRepo(repo) {
    let repoId = "repoId=" + repo.id;
    let url = process.env.VUE_APP_ROOT_API + "/repos/one?" + repoId;
    return axios.get(url).then(response => {
      return response.data
    });
  },

  getBranches(repo) {
    let url = process.env.VUE_APP_ROOT_API + "/branches?repoId=" + repo.id;
    return axios.get(url).then(response => {
      return response.data
    });
  },

  getCommits(repo, branchName, commitId) {
    let repoId = "repoId=" + repo.id + "&";
    branchName = "branchName=" + branchName + "&";
    commitId = "commitId=" + commitId + "&";
    let size = "size=" + 30;
    let url = process.env.VUE_APP_ROOT_API + "/commits?" + repoId + branchName + commitId + size;
    return axios.get(url).then(response => {
      return response.data
    });
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
