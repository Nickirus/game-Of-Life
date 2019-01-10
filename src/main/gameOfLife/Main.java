package main.gameOfLife;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Main extends JFrame implements Runnable {
    private static boolean isOnGameFlg = true;
    private static int WidthOfPlane = 60;
    private static int planePresent[][] = new int[60][60];
    private static int planeFuture[][] = new int[60][60];
    private static int planeDead[][] = new int[60][60];
    private static int planeBirth[][] = new int[60][60];
    private static long hashConditionOfPlanes[] = new long[100];
    private static int countOfGenerations;
    private static int countOfHash;
    private static int speed = 80;
    private static int north, south, west, east, nWest, nEast, sWest, sEast;
    private static int sumOfEight, center;

    private static int planeTopology(int i) {
        if (i > (WidthOfPlane - 1)) i = i - WidthOfPlane;
        if (i < 0) i = WidthOfPlane + i;
        return i;
    }

    private static void planeNavigation() {
        for (int i = 0; i < planePresent.length; i++) {
            for (int j = 0; j < planePresent[i].length; j++) {
                center = planePresent[i][j];
                i = i - 1;
                i = planeTopology(i);
                north = planePresent[i][j];
                i = i + 2;
                i = planeTopology(i);
                south = planePresent[i][j];
                i = i - 1;
                i = planeTopology(i);
                j = j - 1;
                j = planeTopology(j);
                west = planePresent[i][j];
                j = j + 2;
                j = planeTopology(j);
                east = planePresent[i][j];
                j = j - 1;
                j = planeTopology(j);
                i = i - 1;
                i = planeTopology(i);
                j = j - 1;
                j = planeTopology(j);
                nWest = planePresent[i][j];
                j = j + 2;
                j = planeTopology(j);
                nEast = planePresent[i][j];
                i = i + 2;
                i = planeTopology(i);
                j = j - 2;
                j = planeTopology(j);
                sWest = planePresent[i][j];
                j = j + 2;
                j = planeTopology(j);
                sEast = planePresent[i][j];
                i = i - 1;
                i = planeTopology(i);
                j = j - 1;
                j = planeTopology(j);
                sumOfEight = (north + south + west + east + nWest + nEast + sWest + sEast);
                planeChangeCondition(i, j);
            }
        }
    }

    private static void planeChangeCondition(int i, int j) {
        if (center == 1) {
            if (sumOfEight == 2 || sumOfEight == 3) {
                planeFuture[i][j] = center;
            } else {
                planeFuture[i][j] = 0;
                if (planePresent[i][j] == 1) {
                    planeDead[i][j] = 1;
                }
            }
        } else if (sumOfEight == 3) {
            planeFuture[i][j] = 1;
            if (planePresent[i][j] == 0) {
                planeBirth[i][j] = 1;
            }
        }
    }


    private static void changeCounter() {
        if (countOfHash > 99) {
            countOfHash = 0;
        }
        hashConditionOfPlanes[countOfHash] = Arrays.deepHashCode(planePresent);
        checkConditionCounter();
        countOfGenerations++;
        countOfHash++;
    }

    private static void checkConditionCounter() {
        for (int i = 1; i < 30; i++)
            if (countOfHash > i && (hashConditionOfPlanes[countOfHash] == hashConditionOfPlanes[countOfHash - i])) {
                isOnGameFlg = false;
            }
    }

    public static void setPlanePresent(int array[][]) {
        changeCounter();
        for (int i = 0; i < planePresent.length; i++) {
            for (int j = 0; j < planePresent[i].length; j++) {
                planePresent[i][j] = array[i][j];
            }
        }
    }

    public static void setPlaneBirth(int array[][]) {
        for (int i = 0; i < planeBirth.length; i++) {
            for (int j = 0; j < planeBirth[i].length; j++) {
                planeBirth[i][j] = array[i][j];
            }
        }
    }

    public static void setPlaneDead(int array[][]) {
        for (int i = 0; i < planeDead.length; i++) {
            for (int j = 0; j < planeDead[i].length; j++) {
                planeDead[i][j] = array[i][j];
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        while (isOnGameFlg) {
            int x = 10 + 4 + 1 + 5;
            int y = 10 + 26 + 1 + 5;
            super.paint(g);

            Color colorLine = new Color(29, 29, 29);
            g.setColor(colorLine);
            for (; x < 675 + 5; x += 11) g.drawLine(x, 0, x, 700);
            for (; y < 695 + 5; y += 11) g.drawLine(0, y, 680, y);

            Color colorOval = new Color(40, 141, 113);
            g.setColor(colorOval);
            for (int i = 0; i < planePresent.length; i++) {
                for (int j = 0; j < planePresent[i].length; j++) {
                    if (planePresent[i][j] == 1)
                        g.drawOval(11 * j + 4 + 5, 11 * i + 26 + 5, 11, 11);
                }
            }

            Color colorDead = new Color(141, 92, 51);
            g.setColor(colorDead);
            for (int i = 0; i < planeDead.length; i++) {
                for (int j = 0; j < planeDead[i].length; j++) {
                    if (planeDead[i][j] == 1)
                        g.drawOval(11 * j + 4 + 5, 11 * i + 26 + 5, 11, 11);
                }
            }
            run();

            Color colorBirth = new Color(39, 60, 50);
            g.setColor(colorBirth);
            for (int i = 0; i < planeBirth.length; i++) {
                for (int j = 0; j < planeBirth[i].length; j++) {
                    if (planeBirth[i][j] == 1)
                        g.drawOval(11 * j + 4 + 5, 11 * i + 26 + 5, 11, 11);
                }
            }
            run();
        }
    }

    private static void messageGameOver() {
        JOptionPane.showMessageDialog(null,
                "Game over. " + countOfGenerations + " generations. ",
                "Game of Life",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Main() {
        super("Game of Life");
        JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(668 + 10, 690 + 10);
        setVisible(true);
        setResizable(false);

        Color colorBack = new Color(37, 37, 37);
        getContentPane().setBackground(colorBack);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int[][] zeroingArray(int array[][]) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }

    private static void lifeCycle() {
        planeDead = zeroingArray(planeDead);
        planeBirth = zeroingArray(planeBirth);
        setPlanePresent(planePresent);
        planeNavigation();
        setPlaneDead(planeDead);
        setPlaneBirth(planeBirth);
        for (int i = 0; i < planePresent.length; i++) {
            for (int j = 0; j < planePresent[i].length; j++) {
                planePresent[i][j] = planeFuture[i][j];
            }
        }
        try {
            Thread.sleep(speed - 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        Random random = new Random();
        for (int i = 0; i < planeFuture.length; i++) {
            for (int j = 0; j < planeFuture[i].length; j++) {
                planeFuture[i][j] = planePresent[i][j] = random.nextInt(2);
            }
        }

        new Main();
        while (isOnGameFlg) {
            lifeCycle();
        }
        messageGameOver();
    }
}
