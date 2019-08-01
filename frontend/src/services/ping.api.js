import axios from 'axios';
import rxios from "./api.client"

let http = new rxios({}, false, false);

let PingAPI = {
  ping() {
    return http.get(process.env.VUE_APP_ROOT_API + '/ping');
  },
};

export default PingAPI;
