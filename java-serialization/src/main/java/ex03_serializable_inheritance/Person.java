package ex03_serializable_inheritance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Person implements Serializable {

    private String name;
    private int age;
}
