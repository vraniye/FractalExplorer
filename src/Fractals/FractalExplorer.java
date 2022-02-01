package Fractals;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/** Класс предназначеный для создания оконного приложения*/
public class FractalExplorer {
    //Создание кнопок и комбобокса;
    private JComboBox box;
    private JButton btnSave;
    private JButton btnReset;

    //Переменные для определения размера экрана, генерации фрактала и ...
    private int screenSize;
    private JImageDisplay display;
    private FractalGenerator generator;
    private Rectangle2D.Double rectangle; //Прямоугольник
    private int rowsRemaining;

    /** Конструктор класса */
    public FractalExplorer(int size){
        screenSize = size;
        display = new JImageDisplay(screenSize, screenSize);
        generator = new Mandelbrot();
        rectangle = new Rectangle2D.Double();
        generator.getInitialRange(rectangle);
    }

    public void createAndShowGUI(){
        //Создание окна
        JFrame window = new JFrame("Fractal Explorer");
        window.setLayout(new java.awt.BorderLayout());
        window.add(display, BorderLayout.CENTER);
        window.addMouseListener(new MouseList());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Часть 5ой лабораторной работы
        JPanel jp = new JPanel();
        box = new JComboBox();
        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());
        box.addActionListener(new Action(box));
        btnReset = new JButton("Reset Display");
        btnReset.addActionListener(new Action());
        btnSave = new JButton("Save Image");
        btnSave.addActionListener(new Action());
        JPanel jpSouth = new JPanel();
        jpSouth.add(btnReset);
        jpSouth.add(btnSave);
        window.add(jpSouth, BorderLayout.SOUTH);

        jp.add(new JLabel("Fractal: "));
        jp.add(box);
        window.add(jp, BorderLayout.NORTH);

        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }

    /** Метод, предназанченый для отрисовки фрактала*/
    private void drawFractal(){
        enableUI(false);
        rowsRemaining = screenSize;
        for (int x = 0; x < screenSize; x++){
            FractalWorker fw = new FractalWorker(x);
            fw.execute();
        }
    }

    /** Метод, включающий или отключающий возможность взаимдействия с кнопками во время работы с несколькими потоками */
    private void enableUI(boolean val){
        box.setEnabled(val);
        btnReset.setEnabled(val);
        btnSave.setEnabled(val);
    }

    /** Метод, предназаначенный для реагирования окна вцелом на какие-то действия с ним*/
    private class Action implements ActionListener{
        JComboBox box;
        public Action(JComboBox box){
            this.box = box;
        }
        public Action(){
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == box){
                generator = (FractalGenerator) box.getSelectedItem();
                generator.getInitialRange(rectangle);
                drawFractal();
            }
            else if (e.getActionCommand().equals("Reset Display")) {
                generator.getInitialRange(rectangle);
                drawFractal();
            }
            else if (e.getActionCommand().equals("Save Image")){
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int choice = chooser.showSaveDialog(display);
                if (choice == JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();
                    String nameFile = file.toString();
                    try{
                        javax.imageio.ImageIO.write(display.getImage(), "png", file);
                    }catch (IOException ioException) {
                        JOptionPane.showMessageDialog(display, ioException.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /** Метод, преназначенный для увеличения окна при нажатии на него мышью*/
    private class MouseList extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining != 0){
                return;
            }
            double xCoord = FractalGenerator.getCoord (rectangle.x, rectangle.x + rectangle.width, screenSize, e.getX());
            double yCoord = FractalGenerator.getCoord (rectangle.y, rectangle.y + rectangle.height, screenSize, e.getY());
            generator.recenterAndZoomRange(rectangle, xCoord, yCoord, 0.5);
            drawFractal();

        }
    }

    /** Класс, для реализации многопоточности */
    private class FractalWorker extends SwingWorker<Object, Object> {
        //у-координата вычисляемой строки
        int yCord;
        // Массив для хранения вычисленных значений RGB для каждого пикселя в строке
        int[] RGBArr;

        //Конструктор
        private FractalWorker (int yValue){
            yCord = yValue;
        }

        @Override
        protected Object doInBackground() throws Exception {
            RGBArr = new int[screenSize];
            for (int i = 0; i < RGBArr.length; i++){
                double xCoord = FractalGenerator.getCoord (rectangle.x, rectangle.x + rectangle.width, screenSize, i);
                double yCoord = FractalGenerator.getCoord (rectangle.y, rectangle.y + rectangle.height, screenSize, yCord);
                int iterations = generator.numIterations(xCoord, yCoord);
                if (iterations == -1){
                    RGBArr[i] = 0;
                }
                else{
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    RGBArr[i] = rgbColor;
                }
            }
            return null;
        }

        protected void done(){
            for (int i = 0; i < RGBArr.length; i++){
                display.drawPixel(i, yCord, RGBArr[i]);
            }
            display.repaint(0, 0, yCord, screenSize, 1);
            rowsRemaining --;
            if (rowsRemaining == 0){
                enableUI(true);
            }
        }
    }

    /**             @Main               */
    public static void main(String[] args){
        FractalExplorer fc = new FractalExplorer(800);
        fc.createAndShowGUI();
        fc.drawFractal();
    }
}
