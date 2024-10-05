import { Client, StatusOK } from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new Client();
client.load (
    ["./"]
    , "hello.proto"
);

export const options = {
    vus: 1000,
    duration: "10s",
}

export default () => {
  client.connect("localhost:9090", {
      plaintext: true,
      timeout: '10s',
  });

  const data = { firstName: "KIM", lastName: "BYEONGHWA" };
  const response = client.invoke("HelloService/hello", data);

  check(response, {
    'status is OK': (r) => r && r.status === StatusOK,
  });

  console.log(JSON.stringify(response.message));

  client.close();
  sleep(0.5);
};


// 참고: https://github.com/mostafa/performance-testing-grpc-services
