package thread.lecture;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.StringJoiner;

public class ObjectSignalBasic {
    private final int N = 10;
    private final String OUTPUT_FILE = "app/dist/matrics.txt";
    private final String INPUT_FILE =  "app/resources/matrics.txt";


    public void execute() throws IOException {
        // use this if you don't have a input file!
        // MatricesGenerator generator = new MatricesGenerator();
        // generator.execute();

        ThreadSafeQueue queue = new ThreadSafeQueue();

        File inputFile = new File(INPUT_FILE);
        File outFile = new File(OUTPUT_FILE);

        MatricsReaderProducer reader = new MatricsReaderProducer(new FileReader(inputFile), queue);
        MatricsMultiplierConsumer multiplier = new MatricsMultiplierConsumer(new FileWriter(outFile),queue);

        reader.start();
        multiplier.start();
    }


    private class MatricsMultiplierConsumer extends Thread {
        private ThreadSafeQueue queue;
        private FileWriter writer; 

        public MatricsMultiplierConsumer(FileWriter writer,ThreadSafeQueue queue) {
            this.queue = queue;
            this.writer = writer;
        }

        private float[][] multiplyMatrices(float [][] m1, float [][]m2) {
            float[][] result = new float[N][N];
            for (int r = 0; r < N; r++) {
                for(int c = 0; c < N; c++) {
                    for(int k = 0; k < N; k++) {
                        result[r][c] += m1[r][k] * m2[k][c];
                    }
                }
            }
            return result;
        }

        @Override
        public void run(){
            while(true) {
                MatricsPair pair = queue.remove();
                if(pair == null) {
                    System.out.println("No more matrices to read. Consumer Thread is terminating");
                    break;
                }
                float [][] result = multiplyMatrices(pair.matrix1, pair.matrix2);
                
                try {
                    saveMatrixToFile(writer, result);
                } catch (IOException e) {
                }
            }
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
                 
        private void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            for (int r = 0; r < N; r++) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (int c = 0; c < N; c++) {
                    stringJoiner.add(String.format("%.2f", matrix[r][c]));
                }
                fileWriter.write(stringJoiner.toString());
                fileWriter.write('\n');
            }
            fileWriter.write('\n');
        }
    }

    private class MatricsReaderProducer extends Thread {
        private Scanner scanner;
        private ThreadSafeQueue queue;

        public MatricsReaderProducer(FileReader reader, ThreadSafeQueue queue) {
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }

        @Override
        public void run(){
            while(true) {
                float [][] matrix1 = readMatrix();
                float [][] matrix2 = readMatrix();

                if(matrix1 == null || matrix2 == null) {
                    queue.teminate();
                    System.out.println("No more matrices to read. Producer Thread is terminating");
                    return;
                }

                MatricsPair pair = new MatricsPair();
                pair.matrix1 = matrix1;
                pair.matrix2 = matrix2;

                queue.add(pair);
            }
        }

        private float[][] readMatrix() {
            float[][] matrix = new float[N][N];
            for (int i = 0; i < N; i++) {
                if(!scanner.hasNext()) {
                    return null;
                }
                String [] line = scanner.nextLine().split(",");

                for (int j = 0; j < N; j++) {
                    matrix[i][j] = Float.valueOf(line[j]);                    
                }
            }
            scanner.nextLine();
            return matrix;
        }
    }

    private class ThreadSafeQueue {
        private Queue<MatricsPair> queue = new LinkedList<>();
        private boolean isEmpty = true;
        private boolean isTerminate = false;
        private final int CAPACITY = 5; //back pressure to catch up the comsumers speed.

        public synchronized void add(MatricsPair pair) {            
            while (queue.size() == CAPACITY) {
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            queue.add(pair);
            isEmpty = false;
            notify();
        }

        public synchronized MatricsPair remove() {
            MatricsPair matricesPair = null;
            while(isEmpty && !isTerminate) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            if(queue.size() == 1) {
                isEmpty = true;
            }
            if(queue.size() == 0 && isTerminate) {
                return null;
            }
            System.out.println(String.format("queue size %d", queue.size()));
            
            matricesPair = queue.remove();
            if (queue.size() == CAPACITY - 1) {
                notifyAll();
            }
            return matricesPair;
        }

        public synchronized void teminate() {
            isTerminate = true;
            notifyAll();
        }

    }

    private class MatricsPair {
        public float [][] matrix1;
        public float [][] matrix2;
    }

    private class MatricesGenerator {
        private final int NUMBER_OF_MATRIX_PAIRS = 100_000;

        private void execute() {
            File file = new File(INPUT_FILE);
            try (FileWriter writer = new FileWriter(file);) {
                createMatrices(writer);            
                writer.flush();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        private float [] createRow(Random random) {
            float[] row = new float[N];
            for (int i = 0; i < N; i++) {
                row[i] = random.nextFloat() * random.nextInt(100);
            }
            return row;
        }

        private float [][] createMatrix(Random random) {
            float matrix[][] = new float[N][N];
            for (int i = 0; i < N; i++) {
                matrix[i] = createRow(random);
            }
            return matrix;
        }

        private void saveMatrixToFile(FileWriter writer, float[][] matrix) {
            try {
                for (int r = 0; r < N; r++) {
                    StringJoiner stringJoiner = new StringJoiner(", ");
                    for (int c = 0; c < N; c++) {
                        stringJoiner.add(String.format("%.2f", matrix[r][c]));
                    }
                    writer.write(stringJoiner.toString());
                    writer.write('\n');
                }
                writer.write('\n');
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void createMatrices(FileWriter writer){
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_MATRIX_PAIRS * 2; i++) {
                float[][] matrix = createMatrix(random);
                saveMatrixToFile(writer, matrix);
            }
        }
    }
}