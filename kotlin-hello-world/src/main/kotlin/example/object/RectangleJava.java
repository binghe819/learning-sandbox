package example.object;

public class RectangleJava {
    private int height;
    private int width;

    public RectangleJava(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public boolean isSquare() {
        return height == width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
