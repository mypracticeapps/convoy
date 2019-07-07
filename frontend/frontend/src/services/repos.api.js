import axios from 'axios';

let RepoAPI = {
    getRepos(){
        return axios.get('http://localhost:8080/api/v1/repos').then(response=>response.data);
    },

    getBranches(repo){
        let url = "http://localhost:8080/api/v1/branches?repoId="+repo.id;
        return axios.get(url).then(response=> {return response.data});
    },

    getCommits(repo, branch){
        let url = "http://localhost:8080/api/v1/commits?repoId="+repo.id+"&branchName="+branch;
        return axios.get(url).then(response=> {return response.data});
    }
};

export default RepoAPI;