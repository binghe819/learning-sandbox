package com.binghe.springbootjpaexample2.shoppin_mall.presentation;

import com.binghe.springbootjpaexample2.shoppin_mall.application.ItemService;
import com.binghe.springbootjpaexample2.shoppin_mall.application.MemberService;
import com.binghe.springbootjpaexample2.shoppin_mall.application.OrderService;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Item;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Member;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Order;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancel(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch,  Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }
}
