package concurrent.completable_future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Future로 하기 어려웠던 작업들
 * - Future를 외부에서 완료시킬 수 없다. -> 취소하거나, get()에 타임아웃등을 설정해야했다.
 * - 블로킹 메서드 get()를 사용하지않고서는 작업이 끝났을 때 콜백을 실행할 수 없다. (Callback을 지원하지않는다.) - 가장 큰 이유!
 * - 여러 Future를 조합할 수 없다. ex. Event 정보를 가져온 다음 Event에 참석하는 회원 목록 가져오기 (이걸 구현하기가 어렵다. 된다고해도.. 콜백 지옥)
 * - 예외 처리용 API를 제공하지 않는다.
 */
public class CompletableFutureEx_1_future {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);

        Future<String> future = es.submit(() -> "hello");

        future.get(); // blocking

        //
    }
}
