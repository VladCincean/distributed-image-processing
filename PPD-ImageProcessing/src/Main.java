import model.Image;
import model.Pair;
import model.Pixel;
import mpi.Intracomm;
import mpi.MPI;
import mpi.Request;
import processor.ImageProcessor;
import util.BytesUtil;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private static List<Integer> DivideEvenly(int numerator, int denominator) throws Exception
    {
        if (numerator <= 0 || denominator <= 0)
            throw new Exception("Invalid division!");
        List<Integer> array = new ArrayList<>();
        int mod;
        int div = numerator / denominator;
        mod = numerator % denominator;

        for (int i = 0; i < denominator; i++) {
            if(i < mod) {
                array.add(div + 1);
            } else {
                array.add(div);
            }
        }
        return array;

    }
    private static Pair MpiFor(int forStart, int forEnd, int forIncrement, Intracomm communicator)

    {
        int forIterations = (forEnd - forStart) / forIncrement + 1;

        int workers = communicator.Size() - 1;
        List<Integer> workload = new ArrayList<>();
        try {
            workload = DivideEvenly(forIterations, workers);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        int workloadStart = 0;
        int workloadEnd = workload.get(0) - 1;

        for (int i = 1; i < communicator.Rank(); ++i)
        {
            workloadStart += workload.get(i - 1);
            workloadEnd = workloadStart + workload.get(i) - 1;
        }
        return new Pair(workloadStart, workloadEnd);
    }
    public static void main(String[] args) {
        MPI.Init(args);
        Image image = new Image("res/img1.jpg");
        BytesUtil bytesUtil = new BytesUtil();
        byte[] imageBytes = new byte[10000];
        byte[] pixelBytes = new byte[10000];
        Request req;
        if (MPI.COMM_WORLD.Rank() == 0) {
            for (int x = 0; x < image.getWidth() * image.getHeight(); x++) {
                    req = MPI.COMM_WORLD.Irecv(pixelBytes, 0, pixelBytes.length , MPI.BYTE, MPI.ANY_SOURCE, 999);
                    req.Wait();
                    Pixel result = (Pixel) bytesUtil.toObject(pixelBytes);
                    image.setPixel(result.getX(), result.getY(), result);

            }
            System.out.println("done");
            image.saveToFile("res/img1_sepia.jpg");



        } else {

            Pair pair1 = MpiFor(0, image.getWidth(), 1, MPI.COMM_WORLD);


            for (int x = pair1._first; x < pair1._end; ++x) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Pixel p1 = image.getPixel(x, y);
                    int r = Math.min((int)((0.393 * p1.getR()) + (0.769 * p1.getG()) + (0.189 * p1.getB())), 255);
                    int g = Math.min((int)((0.349 * p1.getR()) + (0.686 * p1.getG()) + (0.168 * p1.getB())), 255);
                    int b = Math.min((int)((0.272 * p1.getR()) + (0.534 * p1.getG()) + (0.131 * p1.getB())), 255);

                    Pixel p2 = new Pixel(p1.getA(), r, g, b, x, y);
                   // System.out.println(p2);
                    pixelBytes = bytesUtil.toByteArray(p2);
                    req = MPI.COMM_WORLD.Isend(pixelBytes, 0, pixelBytes.length, MPI.BYTE, 0, 999);
                    req.Wait();
                }
            }



        }


//        image.saveToFile(outputFilename);
//        int noProcesses = args.length;
//        Image image = new Image("res/img1.jpg");

//        ImageProcessor.toGrayscaleAverage(image, "res/img1_gray.jpg");
      //  ImageProcessor.toSepia(image, "res/img1_sepia.jpg");
        MPI.COMM_WORLD.Barrier();
        MPI.Finalize();

    }
}
