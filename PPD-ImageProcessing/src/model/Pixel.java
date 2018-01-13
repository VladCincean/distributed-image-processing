package model;

import java.io.Serializable;

public class Pixel implements Serializable{
    private int alpha;
    private int red;
    private int green;
    private int blue;
    private int x;
    private int y;

    public Pixel(int alpha, int red, int green, int blue, int x, int y) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.x = x;
        this.y = y;
    }

    public int getA() {
        return alpha;
    }

    public int getR() {
        return red;
    }

    public int getG() {
        return green;
    }

    public int getB() {
        return blue;
    }

    public void setA(int alpha) {
        this.alpha = alpha;
    }

    public void setR(int red) {
        this.red = red;
    }

    public void setG(int green) {
        this.green = green;
    }

    public void setB(int blue) {
        this.blue = blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pixel pixel = (Pixel) o;

        if (alpha != pixel.alpha) return false;
        if (red != pixel.red) return false;
        if (green != pixel.green) return false;
        return blue == pixel.blue;
    }

    @Override
    public int hashCode() {
        int result = alpha;
        result = 31 * result + red;
        result = 31 * result + green;
        result = 31 * result + blue;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
