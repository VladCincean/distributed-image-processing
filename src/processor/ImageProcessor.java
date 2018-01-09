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
}
