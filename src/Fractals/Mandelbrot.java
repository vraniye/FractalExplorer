package Fractals;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{

    /** Константа с максимальным количествомитераций */
    public static final int MAX_ITERATIONS = 2000;

    /** Этот метод позволяет генератору
     фракталов определить наиболее «интересную» область комплексной плоскости
     для конкретного фрактала */
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /** Этот метод реализует итеративную
     функцию для фрактала Мандельброта */
    @Override
    public int numIterations(double x, double y) {
        //Переменные для подсчета кол-ва итераций, значения действительной и мнимой части комплексного числа.
        int iterations = 0;
        double real = 0;
        double notReal = 0;
        while (iterations < MAX_ITERATIONS && ((real*real + notReal*notReal) < 4)){
            double carryVariable = real;
            real = real*real - notReal*notReal + x;
            notReal = 2 * carryVariable * notReal + y;
            iterations++;
        }
        if (iterations == MAX_ITERATIONS){
            return -1;
        }
        else return iterations;
    }

    /** Метод toString для отображения названия фрактала в дальнейшем */
    public String toString(){
        return "Mandelbrot";
    }
}
