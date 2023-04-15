package ex03_serializable_inheritance;

import lombok.Getter;

@Getter
public class SoftwareDeveloper extends Person {

    private String language;

    public SoftwareDeveloper(String name, int age, String language) {
        super(name, age);
        this.language = language;
    }
}
