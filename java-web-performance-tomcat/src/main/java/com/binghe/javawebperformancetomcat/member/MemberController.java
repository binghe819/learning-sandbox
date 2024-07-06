package com.binghe.javawebperformancetomcat.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/member/short")
    public ResponseEntity<Member> getMemberShortText() {
        Member member = Member.builder()
                .name("short")
                .age(30)
                .country("South Korea")
                .description("short text")
                .build();
        return ResponseEntity.ok(member);
    }

    @GetMapping("/member/long")
    public ResponseEntity<Member> getMemberLongText() {
        String loremIpsum = "What is Lorem Ipsum?\n" +
                "Lorem Ipsum is a type of placeholder text used in the printing and typesetting industry. It has been the standard dummy text since the 1500s, originating when an unknown printer scrambled a set of type to create a type specimen book. Lorem Ipsum has survived through the centuries, even adapting to electronic typesetting without significant changes. Its popularity surged in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and it continues to be widely used in modern desktop publishing software such as Aldus PageMaker.\n" +
                "Why do we use it?\n" +
                "The use of Lorem Ipsum is based on the idea that a reader will be distracted by the actual content of a page when looking at its layout. By using Lorem Ipsum, which mimics the distribution of letters in English without forming readable text, designers can focus on the visual aspects of a page. It is now the default placeholder text in many desktop publishing packages and web page editors. Searching for 'lorem ipsum' online will reveal numerous websites in their early stages of development. Over time, various versions of Lorem Ipsum have emerged, sometimes altered intentionally or accidentally with added humor.\n" +
                "Where does it come from?\n" +
                "Contrary to popular belief, Lorem Ipsum is not just random text. Its origins trace back to classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, discovered its source by researching the Latin word \"consectetur\" found in a Lorem Ipsum passage. He identified the text as coming from sections 1.10.32 and 1.10.33 of Cicero's \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil), a treatise on ethics popular during the Renaissance. The famous first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet,\" originates from section 1.10.32 of this work.\n" +
                "The Standard Chunk\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is provided below for those interested. Sections 1.10.32 and 1.10.33 from Cicero's \"de Finibus Bonorum et Malorum\" are reproduced in their original Latin form, along with English translations from the 1914 version by H. Rackham.\n" +
                "Where can I get some?\n" +
                "There are many versions of Lorem Ipsum passages available, though many have been altered with injected humor or random words that don't appear believable. If you choose to use a Lorem Ipsum passage, ensure it doesn't contain any hidden, embarrassing text. The Internet hosts numerous Lorem Ipsum generators that often repeat predefined chunks, making this generator one of the first true sources. It uses a dictionary of over 200 Latin words and several model sentence structures to create Lorem Ipsum that looks reasonable, ensuring it is free from repetition, humor, or non-characteristic words.\n";

        Member member = Member.builder()
                .name("long")
                .age(30)
                .country("South Korea")
                .description(loremIpsum)
                .build();
        return ResponseEntity.ok(member);
    }
}
