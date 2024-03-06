package JavaToKt_8_abstract;

public class JavaCat extends AbstractJavaAnimal {

    public JavaCat(String species, int legCount) {
        super(species, 4);
    }

    @Override
    public void move() {
        System.out.println("고양이가 네 발로 걷습니다.");
    }
}
