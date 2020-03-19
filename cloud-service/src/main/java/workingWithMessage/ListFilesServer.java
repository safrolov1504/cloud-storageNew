package workingWithMessage;

import Connection.CreatCommand;
import Connection.FileForTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class ListFilesServer {
    public static byte[] creatFileList(String userName) {
        File folder = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-service/global-storage/"+userName);
        File[] arrayFile = folder.listFiles();
        BasicFileAttributes attr;
        StringBuilder sb = new StringBuilder();
        FileForTable fileForTable = new FileForTable();
        try {
            for (File f:arrayFile) {
                attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                fileForTable = new FileForTable(f.getName(),attr.size()+"",attr.creationTime().toString());
                //System.out.println(fileForTable.toString());
                sb.append(fileForTable.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        System.out.println(Arrays.toString(sb.toString().getBytes()));
        return sb.toString().getBytes();
    }
}
