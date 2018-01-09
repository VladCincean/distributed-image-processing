package processor;

import model.Image;
import model.Pixel;

public class ImageProcessor {

    private ImageProcessor() {
    }

    /**
     * Applies a grayscale filter by using the average method
     * @param image - input image
     * @param outputFilename - output image file name
     */
    public static void toGrayscaleAverage(Image image, String outputFilename) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Pixel p1 = image.getPixel(x, y);

                int gray = (p1.getR() + p1.getG() + p1.getB()) / 3;

                Pixel p2 = new Pixel(p1.getA(), gray, gray, gray);

                image.setPixel(x, y, p2);
            }
        }

        image.saveToFile(outputFilename);
    }

    /**
     * Applies a sepia filter
     * @param image - input image
     * @param outputFilename - output image file name
     */
    public static void toSepia(Image image, String outputFilename) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Pixel p1 = image.getPixel(x, y);

                int r = Math.min((int)((0.393 * p1.getR()) + (0.769 * p1.getG()) + (0.189 * p1.getB())), 255);
                int g = Math.min((int)((0.349 * p1.getR()) + (0.686 * p1.getG()) + (0.168 * p1.getB())), 255);
                int b = Math.min((int)((0.272 * p1.getR()) + (0.534 * p1.getG()) + (0.131 * p1.getB())), 255);

                Pixel p2 = new Pixel(p1.getA(), r, g, b);

                image.setPixel(x, y, p2);
            }
        }

        image.saveToFile(outputFilename);
    }
}
