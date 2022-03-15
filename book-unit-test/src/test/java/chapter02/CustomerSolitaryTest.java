package chapter02;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Mockist 테스트
 */
@ExtendWith(MockitoExtension.class)
public class CustomerSolitaryTest {

    @Test
    void 재고가_충분할_때_구매_성공() {
        // given
        Product product = new Product("Shampoo");
        Customer customer = new Customer();
        Store store = Mockito.mock(Store.class);

        // mock
        given(store.hasEnoughInventory(product, 5)).willReturn(true);

        // when
        boolean actual = customer.purchase(store, product, 5);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 재고가_충분하지_않아서_구매_실패() {
        // given
        Product product = new Product("Shampoo");
        Customer customer = new Customer();
        Store store = Mockito.mock(Store.class);

        // mock
        given(store.hasEnoughInventory(product, 5)).willReturn(false);

        // when
        boolean actual = customer.purchase(store, product, 5);

        // then
        assertThat(actual).isFalse();
    }
}
