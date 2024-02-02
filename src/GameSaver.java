import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GameSaver {

    public static void main(String[] args) {
        Random rndm = new Random();
        String pathSaveGames = "D://Games/savegames/";

        List<GameProgress> games = new ArrayList<>();
        // (int health, int weapons, int lvl, double distance)
        games.add(new GameProgress(
                rndm.nextInt(100), rndm.nextInt(15), 1, rndm.nextDouble(30.0)));
        games.add(new GameProgress(
                rndm.nextInt(100), rndm.nextInt(15), 2, rndm.nextDouble(30.0)));
        games.add(new GameProgress(
                rndm.nextInt(100), rndm.nextInt(15), 3, rndm.nextDouble(30.0)));

        List<String> filesToZip = new ArrayList<>();

        int index = 1;
        for (GameProgress game : games) {
            String saveFileName = "save" + index++ + ".dat";
            saveGame(pathSaveGames + saveFileName, game);
            filesToZip.add(pathSaveGames + saveFileName);
        }

        zipFiles(pathSaveGames + "saves.zip", filesToZip);

    }


    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf('/') + 1));
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (String file : files) {
            File deleteFile = new File(file);
            if (deleteFile.delete()) {
                System.out.println(deleteFile.getAbsolutePath() + "-> has been deleted");
            }
        }

    }


}
