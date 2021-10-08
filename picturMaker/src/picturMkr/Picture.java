package picturMkr;

import java.util.Scanner;
import java.io.IOException;
import java.io.IOError;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.lang.ArrayIndexOutOfBoundsException;
import java.awt.Graphics2D;

public class Picture {
    int h = 0;
    int w = 0;
    String filename = null;
    int mode = 0;
    BufferedImage bimg = null;
    String line = " ";
    String v = " ";

    public static void main(String[] args) {
        Picture pic = new Picture();
        pic.picture();
    }

    public void picture() {
        File dir = new File("ref/");
        String[] content = dir.list();

        if (content == null) {
            System.out.println("The refrence (ref/) directory is empty or missing!");
            System.exit(1);
        } else {
            for (int i = 0; i < content.length; i++) {
                filename = content[i];
                Process(filename);
            }
            System.out.println("quitting...");
        }
    }

    public void Process(String x) {
        try {
            bimg = ImageIO.read(new File("ref/" + x));
            h = bimg.getHeight();
            w = bimg.getWidth();
            System.out.println();
            System.out.println("Size of Picture, W: " + w + ", H: " + h);
            if (w > 160) {
                resChange(bimg);
            }
            h = bimg.getHeight();
            w = bimg.getWidth();
            save(bimg, w, h);
        }
        catch (IOException e) {
            System.out.println("The files in ref are not the correct format!");
            System.out.println(e.getMessage());
            System.exit(2);
        }
    }

    public void save(BufferedImage bimg, int m, int n) {
        String[][] Print = new String[n][1];
        for (int i = 0; i < n; i++) {
            line = "";
            for (int j = 0; j < m; j++) {
                v = getContrast(bimg, j, i);
                line = line + v;
            }
            Print[i][0] = line;
        }
        print(Print);
    }

    public void print(String[][] Print) {
        Scanner sysIN = new Scanner(System.in);
        int op = 0;
        int whil = 0;
        System.out.println();
        System.out.println(filename + ": ");
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.println(Print[i][0]);
        }
        while (whil == 0) {
            System.out.println();
            System.out.println("Type '1' for next and '2' for quit.");
            op = sysIN.nextInt();
            if (op == 1) {
                whil = 1;
            } else if (op == 2) {
                System.out.println("Quitting");
                System.exit(0);
                whil = 1;
            } else {
                System.out.println("Invalid Command!");
            }
        }
    }

    public String getContrast(BufferedImage img, int x, int y) {
        String s = "";
        double con = 0.0;
        int rgb = img.getRGB(x, y);
        double red = (rgb >> 16) & 0x000000FF;
        double green = (rgb >>8 ) & 0x000000FF;
        double blue = (rgb) & 0x000000FF;

        if ((red > blue) && (red > green)) {
            con = red / 255;
        } else if ((blue > red) && (blue > green)) {
            con = blue / 255;
        } else if ((green > blue) && (green > red)) {
            con = green / 255;
        } else {
            con = 0;
        }

        if ((con >= 0.9) && (con < 1.0)) {
            s = "#";
        } else if ((con >= 0.8) && (con < 0.9)) {
            s = "?";
        } else if ((con >= 0.7) && (con < 0.8)) {
            s = "*";
        } else if ((con >= 0.6) && (con < 0.7)) {
            s = "!";
        } else if ((con >= 0.5) && (con < 0.6)) {
            s = "=";
        } else if ((con >= 0.4) && (con < 0.5)) {
            s = ";";
        } else if ((con >= 0.3) && (con < 0.4)) {
            s = ":";
        } else if ((con >= 0.2) && (con < 0.3)) {
            s = ",";
        } else if ((con >= 0.1) && (con < 0.2)) {
            s = ".";
        } else if ((con >= 0.0) && (con < 0.1)) {
            s = " ";
        } else {
            s = "@";
        }

        return s;
    }

    public void resChange(BufferedImage a) {
        int ratio = (w / 160);
        Image tmp = a.getScaledInstance(160, ((h / ratio) / 2), Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(160, ((h / ratio) / 2), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        bimg = dimg;
        h = bimg.getHeight();
        w = bimg.getWidth();
        System.out.println("Scaled to, W: " + w + ", H: " + h);
        System.out.println();
    }
}
