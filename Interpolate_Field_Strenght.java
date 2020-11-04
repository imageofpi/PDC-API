import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.lang.Math;
public class Interpolate_Field_Strenght extends JPanel {
    static JFrame f;

    static void show(char[][] field,String title) {
        f = new JFrame();
        f.setTitle(title);
        int row = field.length, col = field[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                JPanel b = new JPanel();
                if (field[i][j] == 'R')
                    b.setBackground(Color.RED);
                else if (field[i][j] == 'B')
                    b.setBackground(Color.BLUE);
                else
                    b.setBackground(Color.GREEN);
                b.setOpaque(true);
                f.add(b);
            }
        }

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(field.length, field[0].length));
        f.setLocationRelativeTo(null);

        Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
        f.setMaximumSize(DimMax);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        f.setVisible(true);
    }

    static char[][] interpolate(char[][] field, int margin) {
        int row = field.length, col = field[0].length;
        int[][] good = new int[row][col];
        int[][] bad = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                good[i][j] = row * row + col * col + 5;
                bad[i][j] = row * row + col * col + 5;
            }
        }

        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (field[x][y] == 'R') {
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            if (field[i][j] == 'B') {
                                bad[i][j] = Math.min(bad[i][j], (x - i) * (x - i) + (y - j) * (y - j));
                            }
                        }
                    }
                } else if (field[x][y] == 'G') {
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            if (field[i][j] == 'B') {
                                good[i][j] = Math.min(good[i][j], (x - i) * (x - i) + (y - j) * (y - j));
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (field[i][j] == 'B' && (good[i][j] <= margin || bad[i][j] <= margin)) {
                    if (good[i][j] < bad[i][j]) {
                        field[i][j] = 'G';
                    } else {
                        field[i][j] = 'R';
                    }
                }
            }
        }

        return field;
    }

    public static void main(String[] args) {
        int row, col, margin;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter dimensions of field: ");
        row = sc.nextInt();
        col = sc.nextInt();
        System.out.print("Enter the margin of prediction: ");
        margin = sc.nextInt();

        char[][] field = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                field[i][j] = 'B';
            }
        }

        // random method
        Random rand = new Random();
        System.out.print("Enter number of good checkpoints: ");
        int good = sc.nextInt();
        System.out.print("Enter number of bad checkpoints: ");
        int bad = sc.nextInt();
        for (int i = 0; i < good; i++) {
            field[rand.nextInt(row)][rand.nextInt(col)] = 'G';
        }
        for (int i = 0; i < bad; i++) {
            field[rand.nextInt(row)][rand.nextInt(col)] = 'R';
        }

        // System.out.print("Enter no. of chekpoints: ");
        // int checks = sc.nextInt();
        // for(int i=0;i<checks;i++){
        // int x,y;
        // char ch;
        // System.out.print("Enter checkpoint detail: ");
        // x = sc.nextInt();
        // y = sc.nextInt();
        // ch = sc.next().charAt(0);
        // field[x][y] = ch;
        // }
        // test.show(field);

        Interpolate_Field_Strenght.show(field, "Submitted plot");
        field = Interpolate_Field_Strenght.interpolate(field, margin);
        Interpolate_Field_Strenght.show(field, "Interpolated plot");
        sc.close();
    }
}