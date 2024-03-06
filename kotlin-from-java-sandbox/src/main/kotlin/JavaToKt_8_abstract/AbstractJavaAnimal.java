package JavaToKt_8_abstract;

public abstract class AbstractJavaAnimal {

    protected final String species;
    protected final int legCount;

    public AbstractJavaAnimal(String species, int legCount) {
        this.species = species;
        this.legCount = legCount;
    }

    abstract public void move();

    public String getSpecies() {
        return species;
    }

    public int getLegCount() {
        return legCount;
    }
}
