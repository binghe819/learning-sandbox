package ex02_serializable_composition;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Person implements Serializable {

    private String name;
    private int age;
    private transient String description;
    private Address address;
}
