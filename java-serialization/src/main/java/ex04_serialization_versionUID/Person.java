package ex04_serialization_versionUID;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    // 직렬화후 속성 추가
    private String description;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
