import javax.imageio.ImageIO;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class App {
    public static final String SOURCE_FILE = "/home/hoovjar/Workspace/java_concurrency_course/multithreading_4/resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "/home/hoovjar/Workspace/java_concurrency_course/multithreading_4/out/many-flowers.jpg";

    public static void main(String[] args) throws Exception {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // single threaded
        long startTime = System.currentTimeMillis();
        recolorSingleThreaded(originalImage, resultImage);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Single threaded duration: " + String.valueOf(duration));

        // multi threaded with 1 thread
        resultImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        startTime = System.currentTimeMillis();
        recolorMultithreaded(originalImage, resultImage, 1);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Multi threaded with 1 thread duration: " +
                String.valueOf(duration));

        // multi threaded with 2 threads
        resultImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        startTime = System.currentTimeMillis();
        recolorMultithreaded(originalImage, resultImage, 2);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Multi threaded with 2 threads duration: " +
                String.valueOf(duration));

        // multi threaded with 4 threads
        resultImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        startTime = System.currentTimeMillis();
        recolorMultithreaded(originalImage, resultImage, 4);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Multi threaded with 4 threads duration: " +
                String.valueOf(duration));

        // multi threaded with 6 threads
        resultImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        startTime = System.currentTimeMillis();
        recolorMultithreaded(originalImage, resultImage, 6);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Multi threaded with 6 threads duration: " +
                String.valueOf(duration));

        // multi threaded with 8 threads
        resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        startTime = System.currentTimeMillis();
        recolorMultithreaded(originalImage, resultImage, 8);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Multi threaded with 8 threads duration: " + String.valueOf(duration));

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage,
            int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
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

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner,
            int topCorner,
            int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
