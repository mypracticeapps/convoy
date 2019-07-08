import axios from 'axios';

let RepoAPI = {
    getRepos(){
        return axios.get('http://localhost:8080/api/v1/repos').then(response=>response.data);
    },

    getBranches(repo){
        let url = "http://localhost:8080/api/v1/branches?repoId="+repo.id;
        return axios.get(url).then(response=> {return response.data});
    },

    getCommits(repo, searchBy, searchTerm){
        let repoId = "repoId=" + repo.id + "&";
        searchBy = "searchBy=" + searchBy + "&";
        searchTerm = "searchTerm=" + searchTerm + "&";
        let size = "size=" + 30;
        let url = "http://localhost:8080/api/v1/commits?" + repoId + searchBy + searchTerm + size;
        return axios.get(url).then(response=> {return response.data});
    }
};

export default RepoAPI;