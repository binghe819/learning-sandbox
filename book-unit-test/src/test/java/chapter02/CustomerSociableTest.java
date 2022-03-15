package chapter02;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * 클래식파 테스트
 */
public class CustomerSociableTest {

    @Test
    void 재고가_충분할_때_구매_성공() {
        // given
        Product shampoo = new Product("Shampoo"); // 불변 값 객체
        Store store = new Store(new HashMap<>()); // 비공개 의존성
        store.addInventory(shampoo, 10);
        Customer customer = new Customer(); // SUT

        // when
        boolean actual = customer.purchase(store, shampoo, 5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 재고가_충분하지_않아서_구매_실패() {
        // given
        Product shampoo = new Product("Shampoo");
        Store store = new Store(new HashMap<>());
        store.addInventory(shampoo, 5);
        Customer customer = new Customer();

        // when
        boolean actual = customer.purchase(store, shampoo, 10);

        // then
        assertThat(actual).isFalse();
    }
}
