package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private String inFilename;
    private BufferedImage image;

    public Image(String filename) {
        this.inFilename = filename;
        loadFromFile(filename);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Pixel getPixel(int x, int y) {
        int pixel = image.getRGB(x, y);

        byte a = (byte)((pixel >> (3 * 8)) & 0xff);
        byte r = (byte)((pixel >> (2 * 8)) & 0xff);
        byte g = (byte)((pixel >> 8) & 0xff);
        byte b = (byte)(pixel & 0xff);

        return new Pixel(a, r, g, b);
    }

    public void setPixel(int x, int y, Pixel p) {
        int rgb = 0;

        rgb = rgb | (p.getA() << (3 * 8));
        rgb = rgb | (p.getR() << (2 * 8));
        rgb = rgb | (p.getG() << 8);
        rgb = rgb | p.getB();

        image.setRGB(x, y, rgb);
    }

    public void saveToFile(String filename) {
        File f = null;

        try {
            f = new File(filename);
            ImageIO.write(image, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile(String filename) {
        File f = null;

        try {
            f = new File(filename);
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
