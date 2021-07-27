
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class AtlasGenerator {

    public static void main(String[] args) throws IOException {

        if (args.length < 4) {
            System.out.println("Atlas Generator 1.0");
            System.out.println("-------------------------------\n");
            System.out.println("Arguments: ");
            System.out.println("-h  -->  Help");
            System.out.println("-f  -->  File Name");
            System.out.println("-d  -->  file Directory Path");
            System.out.println("-------------------------------\n");
            System.out.println("Usage: ");
            System.out.println("Create .atlas file from an image file: \t-f path.png totalTextureCount Row");
            System.out.println("Create .atlas files from Directory: \t-d path totalTextureCount Row");
            System.out.println("-------------------------------\n");
            System.out.println("Examples: ");
            System.out.println("-f file.png 12 2");
            System.exit(0);
        } else {
            AtlasGenerator ag = new AtlasGenerator();
            int totalTexture = toInteger(args[2]);
            int textureRow = toInteger(args[3]);
            if (args[0].equals("-f")) {
                File f = new File(args[1]);
                if (f.exists() && !f.isDirectory()) {
                    if (totalTexture != -1) {
                        if (textureRow != -1) {
                            ag.generateFile(args[1], totalTexture, textureRow);
                        }
                    }
                }
            } else if (args[0].equals("-d")) {
                File f = new File(args[1]);
                if (f.exists() && f.isDirectory()) {
                    if (totalTexture != -1) {
                        if (textureRow != -1) {
                            ag.generateDirectory(args[1], totalTexture, textureRow);
                        }
                    }
                }
            } else {
                System.out.println("Argument not valid!");
            }
        }
    }

    void generateFile(String filePath, int totalTexture, int textureRow) throws IOException {

        File file = new File(filePath);
        String atlasFileName = file.getName().substring(0, file.getName().indexOf(".")) + ".atlas";

        PrintWriter out = new PrintWriter(new FileWriter(atlasFileName));

        BufferedImage bimg = ImageIO.read(file);
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        out.println(atlasFileName);
        out.println("format: RGBA8888");
        out.println("filter: Nearest,Nearest");
        out.println("repeat: none");

        int texturesInRow = totalTexture / textureRow;
        int textureWidth = w / texturesInRow;
        int textureHeight = h / textureRow;
        int textureXPos = 0;
        for (int i = 1; i <= totalTexture; i++) {
            for (int j = 0; j < texturesInRow; j++) {
                out.println(String.format(String.format("%04d", i)));
                out.println(String.format("  " + "rotate: false"));
                out.println(String.format("  " + "xy: " + (textureWidth * j) + ", " + textureXPos));
                out.println(String.format("  " + "size: " + textureWidth + ", " + textureHeight));
                out.println(String.format("  " + "orig: " + textureWidth + ", " + textureHeight));
                out.println(String.format("  " + "offset: 0, 0"));
                out.println(String.format("  " + "index: -1"));
            }
            textureXPos += textureHeight;
        }
        out.close();
    }

    void generateDirectory(String dirPath, int textureCount, int textureRow) {

    }

    private static int toInteger(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
            return num;
        } catch (NumberFormatException nfe) {
            System.out.println("Argument not valid!");
            return -1;
        }
    }
}
