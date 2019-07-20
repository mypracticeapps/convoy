import axios from 'axios';

console.log(process.env.ROOT_API)

function getCommits(repo, branchName, commitId) {
  let repoId = "repoId=" + repo.id + "&";
  branchName = "branchName=" + branchName + "&";
  commitId = "commitId=" + commitId + "&";
  let size = "size=" + 25;
  let url = process.env.VUE_APP_ROOT_API + "/commits?" + repoId + branchName + commitId + size;
  return axios.get(url).then(response => {
    return response.data
  });
}


let CommitAPI = {
  getCommits
};

export default CommitAPI;
