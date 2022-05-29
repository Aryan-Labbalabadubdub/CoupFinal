package database;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static database.Constants.*;

public class CheckMeta {

    public static void checkImages() throws InterruptedException, IOException {
//        Phase1 : makeDirectories
        if (!Files.exists(Path.of(imagesFolderPath))) {
            new File(imagesFolderPath).mkdirs();
        }
        File[] folderContents = Objects.requireNonNull(new File(imagesFolderPath).listFiles());

//        Phase2 : checkSumAndCleanUp
        if (folderContents.length != 18 || folderSize(new File(imagesFolderPath)) <= imageFolderSizeInf || folderSize(new File(imagesFolderPath)) >= imageFolderSizeSup) {
            for (File file : folderContents) {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            downloadFile(imagesFolderURL, imagesFolderPath + "/images.zip");
        }

//        Phase3 : extractingZip
        File zip = new File(imagesFolderPath + "/images.zip");
        if (zip.exists()) {
            for (File file : folderContents) {
                if (!file.equals(zip)) {
                    if (file.isDirectory()) {
                        FileUtils.deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            new ZipFile(zip).extractAll(imagesFolderPath);
            zip.delete();
        }
    }

    public static void checkOst() throws IOException, InterruptedException {
        //        Phase1 : makeDirectories
        if (!Files.exists(Path.of(ostPath))) {
            new File(ostPath).mkdirs();
        }
        File[] folderContents = Objects.requireNonNull(new File(ostPath).listFiles());

//        Phase2 : checkSumAndCleanUp
        if (folderContents.length != 1 || folderSize(new File(ostPath)) <= ostSizeInf || folderSize(new File(ostPath)) >= ostSizeSup) {
            for (File file : folderContents) {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            downloadFile(ostURL, ostPath + "/Percival_Schuttenbach_The_Musty_Scent_Of_Fresh_Pâté.wav");
        }
    }

    public static void downloadFile(String fileURL, String saveDir) throws InterruptedException {
        final JProgressBar jProgressBar = new JProgressBar();

        Thread progressBar = new Thread(() -> {
            jProgressBar.setMaximum(100000);
            JOptionPane.showOptionDialog(null, jProgressBar, "META download", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{}, null);
        });

        Thread progress = new Thread(() -> {
            try {
                URL url = new URL(fileURL);
                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                long completeFileSize = httpConnection.getContentLength();

                BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                FileOutputStream fos = new FileOutputStream(saveDir);
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                byte[] data = new byte[1024];
                long downloadedFileSize = 0;
                int x;
                while ((x = in.read(data, 0, 1024)) >= 0) {
                    downloadedFileSize += x;

                    // calculate progress
                    final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                    // update progress bar
                    SwingUtilities.invokeLater(() -> jProgressBar.setValue(currentProgress));
                    bout.write(data, 0, x);
                }
                bout.close();
                in.close();
            } catch (IOException ignored) {
            }
        });
        progress.start();
        progressBar.start();
        while (progressBar.isAlive()) {
            Thread.sleep(100);
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
}
