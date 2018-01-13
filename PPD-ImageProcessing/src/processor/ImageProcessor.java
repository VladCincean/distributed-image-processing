package processor;

import model.Image;
import model.Pair;
import model.Pixel;
import mpi.Intracomm;
import mpi.MPI;
import mpi.Request;
import util.BytesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {

    private ImageProcessor() {
    }

    /**
     * Applies a grayscale filter by using the average method
     * @param image - input image
     * @param outputFilename - output image file name
     */
    public static void toGrayscaleAverage(int start, int finish, Image image, String outputFilename) {
        for (int x = start; x < finish; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Pixel p1 = image.getPixel(x, y);

                int gray = (p1.getR() + p1.getG() + p1.getB()) / 3;

                Pixel p2 = new Pixel(p1.getA(), gray, gray, gray, x, y);

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
    public static void toSepia(int start, int finish, Image image, String outputFilename) {

        for (int x = start; x < finish; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Pixel p1 = image.getPixel(x, y);

                int r = Math.min((int)((0.393 * p1.getR()) + (0.769 * p1.getG()) + (0.189 * p1.getB())), 255);
                int g = Math.min((int)((0.349 * p1.getR()) + (0.686 * p1.getG()) + (0.168 * p1.getB())), 255);
                int b = Math.min((int)((0.272 * p1.getR()) + (0.534 * p1.getG()) + (0.131 * p1.getB())), 255);

                Pixel p2 = new Pixel(p1.getA(), r, g, b,x,y);

                image.setPixel(x, y, p2);
            }
        }

        image.saveToFile(outputFilename);
    }

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
        return new Pair(workloadStart, workloadEnd );
    }

    public static void applyFilterWithMPI(String[] args) {
        MPI.Init(args);
        long startTime = System.nanoTime();

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
            image.saveToFile("res/img1_sepia.jpg");
            long endTime = System.nanoTime();
            long totalDuration = (endTime - startTime);
            System.out.println("The duration in miliseconds:");
            System.out.println(totalDuration/1000000000.0);
        } else {

            Pair pair1 = MpiFor(0, image.getWidth(), 1, MPI.COMM_WORLD);
            int end =  pair1._end;

            if(pair1._end == image.getWidth()) {
                end--;
            }
            for (int x = pair1._first; x <= end; x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Pixel p1 = image.getPixel(x, y);
                    int r = Math.min((int)((0.393 * p1.getR()) + (0.769 * p1.getG()) + (0.189 * p1.getB())), 255);
                    int g = Math.min((int)((0.349 * p1.getR()) + (0.686 * p1.getG()) + (0.168 * p1.getB())), 255);
                    int b = Math.min((int)((0.272 * p1.getR()) + (0.534 * p1.getG()) + (0.131 * p1.getB())), 255);

                    Pixel p2 = new Pixel(p1.getA(), r, g, b, x, y);
                    pixelBytes = bytesUtil.toByteArray(p2);
                    req = MPI.COMM_WORLD.Isend(pixelBytes, 0, pixelBytes.length, MPI.BYTE, 0, 999);
                    req.Wait();
                }
            }
        }
        MPI.COMM_WORLD.Barrier();
        MPI.Finalize();


    }


    public static void applyFilterUsingThreads() {
        Scanner reader = new Scanner(System.in);
        Image image = new Image("res/img1.jpg");
        System.out.println("gIve a number of threads: ");
        int noThreads = reader.nextInt(); // number of threads
        final int nr = image.getWidth()/noThreads; // nr of elements(lines of matrix) in intervals if equal division is possible
        int rest = image.getWidth() % noThreads; //nr of elements to be redistributed(if equal division is not possible
        ExecutorService executor = Executors.newFixedThreadPool(noThreads);
        int j = 0;
        long startTime = System.nanoTime();
        while(rest > 0) {
            final int c1 = j;

            executor.execute(new Thread(() -> toSepia(c1, c1 + nr + 1, image, "res/img1_sepia.jpg")));
            rest --;
            noThreads--;
            j = j + nr + 1;
        }

        for(int i = 0 ; i < noThreads ; i ++) {
            final int c2 = j;
            executor.execute(new Thread(() -> toSepia(c2, c2 + nr, image, "res/img1_sepia.jpg")));
            j = j + nr ;
        }
        executor.shutdown();
         while(!executor.isTerminated()) {}
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long totalDuration = (endTime - startTime);
        System.out.println("The duration in miliseconds:");
        System.out.println(totalDuration/1000000000.0);
    }
}
