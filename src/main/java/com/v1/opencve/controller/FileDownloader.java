package com.v1.opencve.controller;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileDownloader {
    public static String[] links = {"https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-modified.json.zip",
            "https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-recent.json.zip",
            "https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-2021.json.zip"};
    public static String output = "/home/aysenurb/Downloads/cvealert/src/main/resources/cve_jsons/";

    public static void downloadCVEFiles() throws IOException {
        for(String link : links){
            String cvefile = link.split("/")[7];
            String fileUrl = link;
            String outputPath = output + cvefile;
            File zipFile = new File(outputPath);
            FileUtils.copyURLToFile(new URL(fileUrl), zipFile);
            unzipFile(outputPath, zipFile);
        }
    }

    public static void unzipFile(String outputPath, File zipFile) throws IOException {
        String fileZip = outputPath;
        File destDir = new File(output);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                zipFile.delete();
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}