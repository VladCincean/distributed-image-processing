import model.Image;
import processor.ImageProcessor;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("res/img1.jpg");

//        ImageProcessor.toGrayscaleAverage(image, "res/img1_gray.jpg");
        ImageProcessor.toSepia(image, "res/img1_sepia.jpg");
    }
}
