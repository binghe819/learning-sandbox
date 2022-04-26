package com.binghe.springbootjpaexample2.shoppin_mall.presentation;

import com.binghe.springbootjpaexample2.shoppin_mall.application.ItemService;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Book;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book(bookForm.getAuthor(), bookForm.getIsbn());
        book.setName(bookForm.getName());
        book.setPrice(book.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
     }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId")Long itemId, Model model) {
        Book item = (Book) itemService.findById(itemId);

        Book book = new Book(item.getAuthor(), item.getIsbn());

        book.setName(item.getName());
        book.setPrice(item.getPrice());
        book.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", book);
        return "items/updateItemForm";
     }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId")Long itemId, @ModelAttribute("form") BookForm form) {
        itemService.update(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        return "redirect:/items";
     }
}
