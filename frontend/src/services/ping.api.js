import axios from 'axios';

let PingAPI = {
  ping() {
    return axios.get(process.env.VUE_APP_ROOT_API + '/ping');
  },
};

export default PingAPI;
