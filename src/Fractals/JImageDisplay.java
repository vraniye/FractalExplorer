package Fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/** Класс, для отображения изображения фрактала */
public class JImageDisplay extends JComponent {

    //Переменная класса BufferedImage, который управляет изображением, содержимое которого можно записать
    private BufferedImage image;

    /** Get`ер для image */
    public BufferedImage getImage() {
        return image;
    }

    /** Конструктор класса */
    public JImageDisplay(int width, int height){
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Класс Dimension позволяет инкапсулировать ширину и длину объетка в одну переменную
        Dimension dm = new Dimension(width, height);
        super.setPreferredSize(dm);
    }

    /** Метод для отрисовки ихображения */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    /** Метод, который устанавливает все пикселии зображения в черный цвет */
    public void clearImage(){
        int[] rgbArr = new int[getWidth() * getHeight()];
        image.setRGB(0, 0, getWidth(), getHeight(), rgbArr, 0, 0);
    }

    /** Метод, который устанавливает пиксель в определенный цвет */
    public void drawPixel (int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }
}
