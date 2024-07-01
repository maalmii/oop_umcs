public class Main {
    public static void main(String[] args) {
        ImageHandler handler = new ImageHandler();

        // Завантаження зображення
        String imagePath = "C:/Users/User/Pictures/example.jpg";
        handler.loadImage(imagePath);
        handler.increaseBrightness(50);
        // Збереження зображення
        String savePath = "C:/Users/User/Documents/saved_image.jpg";
        handler.saveImage(savePath);
    }
}
