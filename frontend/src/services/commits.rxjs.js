import rxios from "./api.client"
import {retryWhen, delay, take} from 'rxjs/operators'

let http = new rxios({}, true, true);

function getCommits(repo, branchName, commitId) {
  let repoId = "repoId=" + repo.id + "&";
  branchName = "branchName=" + branchName + "&";
  commitId = "commitId=" + commitId + "&";
  let size = "size=" + 25;
  let url = process.env.VUE_APP_ROOT_API + "/commits?" + repoId + branchName + commitId + size;
  return http.get(url).pipe(
    retryWhen(errors => errors.pipe(delay(2000), take(3)))
  );
}

let CommitAPI = {
  getCommits
};

export default CommitAPI;
