package sample;


import java.io.*;

/**
 * Created by weiss on 12/22/2016.
 */
public class FileHandling {
    public static void save(File file, Astaralgorithm alg) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.println(alg.width);
            writer.println(alg.height);
            writer.println();
            for (int i = 0; i < alg.width; i++) {
                for (int j = 0; j < alg.height; j++) {

                    writer.println(alg.tile[i][j].walkable);
                    writer.println(alg.tile[i][j].isStart());
                    writer.println(alg.tile[i][j].isTarget());
                    writer.println();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
    }

    public static Astaralgorithm load(File file) {
        FileReader fileReader = null;

        Astaralgorithm alg = null;

        try {
            fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            int freeSpaces = 0;

            int width = Integer.valueOf(reader.readLine().toString());
            int height = Integer.valueOf(reader.readLine().toString());

            alg = new Astaralgorithm(width, height);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; ) {
                    if (reader.readLine() != null) {
                        alg.tile[i][j].setWalkable(Boolean.valueOf(reader.readLine()));
                        alg.tile[i][j].setStart(Boolean.valueOf(reader.readLine()));
                        alg.tile[i][j].setTarget(Boolean.valueOf(reader.readLine()));

                        j++;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return alg;

    }
}
