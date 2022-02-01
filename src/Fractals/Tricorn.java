package Fractals;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator{

    /** Константа с максимальным количествомитераций */
    public static final int MAX_ITERATIONS = 2000;

    /** Этот метод позволяет генератору
     фракталов определить наиболее «интересную» область комплексной плоскости
     для конкретного фрактала */
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    /** Этот метод реализует итеративную
     функцию для фрактала Мандельброта */
    @Override
    public int numIterations(double x, double y) {
        int iterations = 0;
        double real = 0;
        double notReal = 0;
        while (iterations < MAX_ITERATIONS && ((real*real + notReal*notReal) < 4)){
            double carryVariable = real;
            real = real*real - notReal*notReal + x;
            notReal = -2 * carryVariable * notReal + y;
            iterations++;
        }
        if (iterations == MAX_ITERATIONS){
            return -1;
        }
        else return iterations;
    }

    public String toString(){
        return "Tricorn";
    }
}
