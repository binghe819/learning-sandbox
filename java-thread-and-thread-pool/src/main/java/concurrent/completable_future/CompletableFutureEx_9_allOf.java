package concurrent.completable_future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * allOf()
 * - 여러 작업을 모두 실행하고 모든 작업 결과에 콜백 실행
 * - 여러 작업의 결과 타입이 모두 같을 수 없기때문에, 결과값이 Void이다. 물론 아래와 같이 야매로 결과값을 한번에 얻는 방법도 있다.
 */
public class CompletableFutureEx_9_allOf {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });

        List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray) // futuresArray로 주어진 Future들이 모두 끝났다는 것을 보장.
                .thenApply(v -> futures.stream()
                        .map(stringCompletableFuture -> stringCompletableFuture.join())
                        .collect(Collectors.toList()));

        results.get().forEach(System.out::println);
    }
}
