package com.binghe.springresttemplate.resttemplate;

import com.binghe.springresttemplate.user.application.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resttemplate/users")
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(restTemplateService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(restTemplateService.getAllUser());
    }

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody UserDto userDto) {
        Long userId = restTemplateService.saveByPostForObject(userDto.getName(), userDto.getAge());
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/entity")
    public ResponseEntity<Long> register_entity(@RequestBody UserDto userDto) {
        Long userId = restTemplateService.saveByPostForEntity(userDto.getName(), userDto.getAge());
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/exchange")
    public ResponseEntity<Long> register_exchange(@RequestBody UserDto userDto) {
        Long userId = restTemplateService.saveByExchange(userDto.getName(), userDto.getAge());
        return ResponseEntity.ok(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto response = restTemplateService.update(id, userDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        restTemplateService.delete(id);
        return ResponseEntity.ok("deleted");
    }
}
