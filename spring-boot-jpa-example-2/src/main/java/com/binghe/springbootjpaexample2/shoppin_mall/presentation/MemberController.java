package com.binghe.springbootjpaexample2.shoppin_mall.presentation;

import com.binghe.springbootjpaexample2.shoppin_mall.application.MemberService;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Address;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new Memberform());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid Memberform memberform, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberform.getCity(), memberform.getStreet(), memberform.getZipcode());
        Member member = new Member(memberform.getName(), address);
        memberService.register(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
