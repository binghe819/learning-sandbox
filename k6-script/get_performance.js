import http from "k6/http"
import { sleep } from "k6"

export let options = {
    vus: 100,
    duration: '1m'
    // stages: [
    //     { duration: '10s', target: 10 },
    //     { duration: '10s', target: 20 },
    //     { duration: '10s', target: 30 },
    //     { duration: '10s', target: 0 },
    // ],
}

const BASE_URL = "http://localhost:8080/member/1";

export default function() {
    let getUrl = BASE_URL;

    http.get(getUrl);

    // sleep(1);
}
