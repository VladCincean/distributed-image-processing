package model;

public class Pixel {
    private byte alpha;
    private byte red;
    private byte green;
    private byte blue;

    public Pixel(byte alpha, byte red, byte green, byte blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getA() {
        return alpha;
    }

    public byte getR() {
        return red;
    }

    public byte getG() {
        return green;
    }

    public byte getB() {
        return blue;
    }

    public void setA(byte alpha) {
        this.alpha = alpha;
    }

    public void setR(byte red) {
        this.red = red;
    }

    public void setG(byte green) {
        this.green = green;
    }

    public void setB(byte blue) {
        this.blue = blue;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Pixel pixel = (Pixel) object;

        if (alpha != pixel.alpha) return false;
        if (red != pixel.red) return false;
        if (green != pixel.green) return false;
        return blue == pixel.blue;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) alpha;
        result = 31 * result + (int) red;
        result = 31 * result + (int) green;
        result = 31 * result + (int) blue;
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "Pixel{" +
                "alpha=" + alpha +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
