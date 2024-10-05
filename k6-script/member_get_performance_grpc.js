import { Client, StatusOK } from 'k6/x/grpc';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

export let errorRate = new Rate('errors');
export let apiLatency = new Trend('Latency');

const client = new grpc.Client();
client.load (
    ["./"]
    , "member.proto"
);

export let options = {
    vus: 100,
    // iterations: 1000
    duration: '10s'
    // stages: [
    //     { duration: '10s', target: 10 },
    //     { duration: '10s', target: 20 },
    //     { duration: '10s', target: 30 },
    //     { duration: '10s', target: 0 },
    // ],
};

export default () => {
  client.connect("localhost:9090", {
      plaintext: true,
      timeout: '10s',
  });

  const data = { id: 1 };
  const response = client.invoke("MemberGrpcService/getMember", data);

  check(response, {
    'status is OK': (r) => r && r.status === StatusOK,
  }) || errorRate.add(1);

//   console.log(JSON.stringify(response.message));

  client.close();
  sleep(1);
};


// 참고: https://github.com/mostafa/performance-testing-grpc-services
