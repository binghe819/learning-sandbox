package ex05_serialization_metainfo;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private long age;
    private Address address;

    public Person(String name, long age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
}
