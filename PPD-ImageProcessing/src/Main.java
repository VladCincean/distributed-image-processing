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
    public static void main(String[] args) {
        //ImageProcessor.applyFilterWithMPI(args);
        ImageProcessor.applyFilterUsingThreads();

    }
}
