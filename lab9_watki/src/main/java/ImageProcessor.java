import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//ГІСТОГАМИ БЛІН???????????????????????
//Napisz metodę, która w oparciu o pulę wątków obliczy histogram wybranego kanału obrazu.


public class ImageProcessor {
    private BufferedImage image;

    public ImageProcessor(BufferedImage image) {
        this.image = image;
    }

    /**
     * Метод для обчислення гістограми обраного каналу зображення з використанням пулі потоків.
     *
     * @param channel   Номер каналу (0 - червоний, 1 - зелений, 2 - синій)
     * @param numThreads Кількість потоків у пулі
     * @return Масив, що містить значення гістограми для кожного інтенсивності від 0 до 255
     */
    public int[] calculateChannelHistogram(int channel, int numThreads) {
        // Перевірка коректності номеру каналу
        if (channel < 0 || channel > 2) {
            throw new IllegalArgumentException("Invalid channel number. Must be 0 (red), 1 (green), or 2 (blue).");
        }

        // Ініціалізація масиву для збереження гістограми
        int[] histogram = new int[256]; // 256 можливих значень інтенсивності (0-255)

        // Створення пулу потоків з фіксованою кількістю потоків
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Обчислення висоти одного сегмента
        int heightPerThread = image.getHeight() / numThreads;

        // Запуск завдань для кожного потоку
        for (int i = 0; i < numThreads; i++) {
            int startY = i * heightPerThread;
            int endY = (i == numThreads - 1) ? image.getHeight() : (i + 1) * heightPerThread;

            // Виконання завдання для поточного сегмента
            executor.submit(() -> {
                calculateHistogramForChannel(channel, startY, endY, histogram);
            });
        }

        // Завершення роботи пулу потоків після виконання завдань
        executor.shutdown();

        try {
            // Очікування завершення роботи всіх потоків протягом 10 секунд
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return histogram;
    }

    /**
     * Метод для обчислення гістограми обраного каналу для заданого сегмента зображення.
     *
     * @param channel Номер каналу (0 - червоний, 1 - зелений, 2 - синій)
     * @param startY  Початкова координата Y сегмента
     * @param endY    Кінцева координата Y сегмента
     * @param histogram Масив для збереження гістограми
     */
    private void calculateHistogramForChannel(int channel, int startY, int endY, int[] histogram) {
        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Отримання кольору пікселя
                Color color = new Color(image.getRGB(x, y));

                // Отримання значення відповідного каналу
                int value;
                switch (channel) {
                    case 0: // Червоний канал
                        value = color.getRed();
                        break;
                    case 1: // Зелений канал
                        value = color.getGreen();
                        break;
                    case 2: // Синій канал
                        value = color.getBlue();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid channel number.");
                }

                // Інкрементуємо відповідний бін в гістограмі
                synchronized (histogram) {
                    histogram[value]++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Завантаження зображення з файлу
        BufferedImage image = javax.imageio.ImageIO.read(new File("path_to_your_image.jpg"));

        // Створення екземпляру обробника зображення
        ImageProcessor processor = new ImageProcessor(image);

        // Обчислення гістограми червоного каналу з 4 потоками
        int[] redHistogram = processor.calculateChannelHistogram(0, 4);

        // Виведення результатів гістограми (приклад)
        for (int i = 0; i < redHistogram.length; i++) {
            System.out.println("Intensity " + i + ": " + redHistogram[i]);
        }
    }
}
