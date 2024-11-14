package thread.lecture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
public class OptimizationBasic {
    private static final String SOURCE_FILE = "app/resources/many-flowers.jpg";
    private static final String DESTINATION_FILE = "app/dist/many-flowers.jpg"; 
    private PictureRGB pictureRGB = new PictureRGB(); 

    public void execute() {
        try {
            BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
            BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            long startTime = System.currentTimeMillis();
            recolorSingleThread(originalImage, resultImage);
            long endTIme = System.currentTimeMillis();
            System.out.println("singleThread :" + (endTIme-startTime));


            int numberOfThreads = 3;
            long startTimeMulti = System.currentTimeMillis();
            recolorMultiThread(originalImage, resultImage,numberOfThreads);
            long endTimeMulti = System.currentTimeMillis();

            System.out.println("multiThread :" + (endTimeMulti-startTimeMulti));

            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(resultImage, "jpg", outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recolorMultiThread(BufferedImage originImage, BufferedImage resultImage, int numberOfThreads) {
       List<Thread> threads = new ArrayList<>();
       int width = originImage.getWidth();
       int height = originImage.getHeight() / numberOfThreads;

       for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                pictureRGB.recolorImage(originImage, resultImage, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
       }

       for (Thread thread : threads) {
            thread.start();
       }

       for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO: handle exception
            }
       }
    }

    private void recolorSingleThread(BufferedImage originImage, BufferedImage resultImage) {
        pictureRGB.recolorImage(originImage, resultImage, 0, 0, originImage.getWidth(), originImage.getHeight());
    }

    public class PictureRGB {
        public void recolorImage(BufferedImage originImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
            for (int x = leftCorner; x < leftCorner + width && x < originImage.getWidth(); x++) {
                for (int y = topCorner; y < topCorner + height && y < originImage.getHeight(); y++) {
                    recolorPixel(originImage, resultImage, x, y);
                }
            }
        }

        private void recolorPixel(BufferedImage originImage, BufferedImage resultImage, int x, int y) {
            int rgb = originImage.getRGB(x, y);

            int red = getRed(rgb);
            int green = getGreen(rgb);
            int blue = getBlue(rgb);

            int newRed;
            int newGreen;
            int newBlue;

            if(isShadeOfGray(red, green, blue)) {
                newRed =  Math.min(red + 10,255);
                newGreen =  Math.max(green - 80,0);
                newBlue =  Math.max(blue - 20,0);

            } else {
                newRed = red;
                newBlue = blue;
                newGreen = green;
            }

            int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
            setRGB(resultImage, x,y,newRGB);
        }

        private void setRGB(BufferedImage image, int x, int y, int rgb) {
            image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb,null));
        }

        private boolean isShadeOfGray(int red, int green, int blue) {
            return Math.abs(red - green) < 30 && Math.abs(red-blue) < 30 && Math.abs(green - blue) < 30;
        }

        private int createRGBFromColors(int red, int green, int blue) {
            int rgb = 0;

            rgb |= blue;
            rgb |= green << 8;
            rgb |= red << 16;

            rgb |= 0xFF000000;

            return rgb;

        }

        private int getRed(int rgb) {
            return (rgb & 0x00FF0000) >> 16;
        }
        private int getGreen(int rgb) {
            return (rgb & 0x0000FF00) >> 8;
        }
        private int getBlue(int rgb) {
            return (rgb & 0x000000FF);
        }
    }
}
