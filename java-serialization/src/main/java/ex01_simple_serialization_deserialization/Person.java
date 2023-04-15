package ex01_simple_serialization_deserialization;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Person implements Serializable {
    public static final Person EMPTY = new Person("EMPTY", 0, "EMPTY");

    private String name;
    private int age;
    private transient String description;
}
