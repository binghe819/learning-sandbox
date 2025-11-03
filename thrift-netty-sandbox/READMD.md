# Apache Thrift + Netty 학습 테스트

<br>

# TSocket vs TFramedTransport

| 항목            | **TSocket**                         | **TFramedTransport**              |
| ------------- | ----------------------------------- | --------------------------------- |
| 전송 형태         | 바이트 스트림(경계 없음)                      | `[length(4byte)][payload]` 프레임    |
| 사용 맥락         | 동기/블로킹 서버·클라이언트(전통적인 Thrift Server) | 비동기/논블로킹, 멀티플렉싱(in-flight>1)      |
| 메시지 경계 인식     | 프로토콜(TBinary/Compact)이 읽어가며 “끝”을 판단 | **길이 필드**로 정확히 한 번에 잘림            |
| Netty에서 권장 여부 | 비권장(복잡/난이도↑)                        | **강력 권장**(간단/안전)                  |
| 프레이머 필요       | 커스텀(사실상 구현 난이도 큼)                   | `LengthFieldBasedFrameDecoder`로 끝 |
| 헤더/변환         | 없음                                  | (기본 없음, 단 HeaderTransport는 별도)    |



