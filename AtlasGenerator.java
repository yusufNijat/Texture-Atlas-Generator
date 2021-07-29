
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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
            System.out.println("Create .atlas file from an image file: \t-f path.png textureWidth(px) textureheight(px)");
            System.out.println("Create .atlas files from Directory: \t-d path textureWidth textureheight");
            System.out.println("-------------------------------\n");
            System.out.println("Examples: ");
            System.out.println("-f file.png 100 55");
            System.exit(0);
        } 
        else {
            AtlasGenerator ag = new AtlasGenerator();
            int textureWidth = toInteger(args[2]);
            int textureHeight = toInteger(args[3]);
            Path path = Path.of(args[1]);
            if (args[0].equals("-f")) {
                if (Files.exists(path) && !Files.isDirectory(path)) {
                    if (textureWidth != -1 && textureHeight != -1) {
                        ag.generateFile(path, textureWidth, textureHeight);
                    }
                }
            } else if (args[0].equals("-d")) {
                if (Files.exists(path) && Files.isDirectory(path)) {
                    if (textureWidth != -1 && textureHeight != -1) {
                        ag.generateDirectory(path, textureWidth, textureHeight);
                    }
                }
            } else {
                System.out.println("Argument not valid!");
            }
        }
    }

    public void generateFile(Path path, int textureWidth, int textureHeight) throws IOException {
        String fileName = path.getFileName().toString();
        String atlasFileName = fileName.substring(0, fileName.indexOf(".")) + ".atlas";

        Path newFile = Files.createFile(path.getParent().resolve(atlasFileName));
        PrintWriter out = new PrintWriter(new FileWriter(newFile.toFile()));

        BufferedImage bimg = ImageIO.read(path.toFile());
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        out.println("../" + fileName);
        out.println("format: RGBA8888");
        out.println("filter: Nearest,Nearest");
        out.println("repeat: none");

        int row = h / textureHeight;
        int col = w / textureWidth;
        int textureYPos = 0;
        int textureCount = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                out.println(String.format(String.format("%04d", textureCount)));
                out.println(String.format("  " + "rotate: false"));
                out.println(String.format("  " + "xy: " + (textureWidth * textureCount) + ", " + textureYPos));
                out.println(String.format("  " + "size: " + textureWidth + ", " + textureHeight));
                out.println(String.format("  " + "orig: " + textureWidth + ", " + textureHeight));
                out.println(String.format("  " + "offset: 0, 0"));
                out.println(String.format("  " + "index: -1"));
                textureCount++;
            }
            textureYPos += textureHeight;
        }
        out.close();
    }

    public void generateDirectory(Path path, int textureWidth, int textureHeight) throws IOException {
        File[] files = path.toFile().listFiles();
        for (File file : files) {
            generateFile(file.toPath(), textureWidth, textureHeight);
        }
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
