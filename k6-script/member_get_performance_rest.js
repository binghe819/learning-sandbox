import http from "k6/http";
import { sleep } from "k6";
import { Rate } from 'k6/metrics';

export const errorRate = new Rate('errors');

export let options = {
    vus: 100,
    iterations: 1000
    // duration: '10s'
    // stages: [
    //     { duration: '10s', target: 10 },
    //     { duration: '10s', target: 20 },
    //     { duration: '10s', target: 30 },
    //     { duration: '10s', target: 0 },
    // ],
};

export default function() {

    const url = 'http://localhost:8080/member/1';
    const params = {
        headers: {
        // 'Authorization': 'Token ffc62b27db68502eebc6e90b7c1476d29c581f4d',
        'Content-Type': 'application/json',
        },
    };
    check(http.get(url, params), {
        'status is 200': (r) => r.status == 200,
    }) || errorRate.add(1);

    sleep(1);
};
