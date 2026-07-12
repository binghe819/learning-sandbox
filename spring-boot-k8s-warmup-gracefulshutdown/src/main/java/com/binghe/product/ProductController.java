package com.binghe.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 웜업 대상이 되는 "실제 서빙 API" 예시.
 *
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * 조회성(부수효과 없는) 샘플 API.
     * 웜업 self-call이 반복 호출해도 안전하도록 데이터 변경/이벤트 발행이 없어야 한다.
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        // 실제로는 DB 조회 등이 일어나는 자리. 여기서는 요청 경로(컨트롤러 → 직렬화)를 태우는 데 집중한다.
        return new Product(id, "product-" + id, id * 1000);
    }

    public record Product(long id, String name, long price) {
    }
}
