import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageHandler {
    private BufferedImage image; // поле для зберігання зображення

    // Метод для завантаження зображення з файлу до поля класу
    public void loadImage(String path) {
        try {
            File file = new File(path);
            image = ImageIO.read(file); // читаємо зображення з файлу
            System.out.println("Зображення завантажено успішно: " + path);
        } catch (IOException e) {
            System.out.println("Помилка завантаження зображення: " + e.getMessage());
        }
    }

    // Метод для збереження зображення з поля класу до файлу за вказаною шляхом
    public void saveImage(String path) {
        try {
            File file = new File(path);
            String format = path.substring(path.lastIndexOf('.') + 1); // отримуємо формат зображення
            ImageIO.write(image, format, file); // записуємо зображення до файлу
            System.out.println("Зображення збережено успішно: " + path);
        } catch (IOException e) {
            System.out.println("Помилка збереження зображення: " + e.getMessage());
        }
    }

    // Метод для збільшення яскравості зображення на задану константу без потоків
    public void increaseBrightness(int constant) {
        // Перебираємо всі пікселі зображення
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y); // отримуємо колір пікселя
                int alpha = (rgba >> 24) & 0xFF; // витягуємо альфа-компоненту (прозорість)
                int red = (rgba >> 16) & 0xFF;   // витягуємо червону компоненту
                int green = (rgba >> 8) & 0xFF;  // витягуємо зелену компоненту
                int blue = rgba & 0xFF;          // витягуємо синю компоненту

                // Збільшуємо яскравість для кожного кольору
                red = Math.min(255, red + constant);
                green = Math.min(255, green + constant);
                blue = Math.min(255, blue + constant);

                // Створюємо новий колір з збільшеною яскравістю
                int newRgba = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newRgba); // встановлюємо новий колір пікселя
            }


        }
    }

    // Метод для збільшення яскравості зображення за допомогою заданої константи та числа потоків
    public void increaseBrightnessWithThreads(int constant, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // створюємо пул потоків

        List<Future<Void>> futures = new ArrayList<>();

        // розділяємо зображення на секції для кожного потоку
        int height = image.getHeight();
        int sectionHeight = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startY = i * sectionHeight;
            int endY = (i == numThreads - 1) ? height : startY + sectionHeight;

            // створюємо задачу для потоку
            Callable<Void> task = () -> {
                increaseBrightnessInSection(startY, endY, constant);
                return null;
            };

            futures.add(executor.submit(task)); // додаємо задачу до виконання
        }

        // чекаємо завершення усіх потоків
        for (Future<Void> future : futures) {
            try {
                future.get(); // очікуємо завершення потоку
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown(); // завершуємо пул потоків
    }

    // Метод для збільшення яскравості зображення у заданому діапазоні пікселів
    private void increaseBrightnessInSection(int startY, int endY, int constant) {
        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xFF;
                int red = (rgba >> 16) & 0xFF;
                int green = (rgba >> 8) & 0xFF;
                int blue = rgba & 0xFF;

                red = Math.min(255, red + constant);
                green = Math.min(255, green + constant);
                blue = Math.min(255, blue + constant);

                int newRgba = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newRgba);
            }
        }
    }
    }
