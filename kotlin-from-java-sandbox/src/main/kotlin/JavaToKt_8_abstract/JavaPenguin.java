package JavaToKt_8_abstract;

public class JavaPenguin extends AbstractJavaAnimal {

    private final int wingCount;

    public JavaPenguin(String species) {
        super(species, 2);
        this.wingCount = 2;
    }

    @Override
    public void move() {
        System.out.println("펭귄이 움직입니다.");
    }

    @Override
    public int getLegCount() {
        return super.getLegCount() + this.wingCount;
    }
}
