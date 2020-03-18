package workingWithMessage;

import io.netty.buffer.ByteBuf;

import javax.script.ScriptEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class AddFile {
    private String nameUser;
    private String fileName;
    private File file;
    private FileOutputStream fileOutputStream;

    public AddFile(ByteBuf buf) {

    }


    public void startWorkFile(byte[] arr) throws IOException {
        //read user name and name of file
        System.out.println("start to get file");
        byte nameUserLength = arr[2];
        nameUser = new String(Arrays.copyOfRange(arr,3,3+nameUserLength));

        byte fileLength = arr[3+nameUserLength];
        fileName = new String(Arrays.copyOfRange(arr,4+nameUserLength,4+nameUserLength+fileLength));

        System.out.println(nameUser+" "+fileName);

        //creat folders if it needs
        file = new File("cloud-server/global-storage/"+nameUser);
        if(!file.exists()){
            file.mkdirs();
        }

        //creat file
        file = new File(file.getPath()+"/"+fileName);
        System.out.println(file.getPath());
        file.createNewFile();

        fileOutputStream = new FileOutputStream(file);
        int off = 2+arr[2]+1+arr[3+arr[2]]+1;
        fileOutputStream.write(Arrays.copyOfRange(arr,off,arr.length));

    }

    public void continueWorkFile(byte[] arr) throws IOException {
        //продолжаем запись файла\
        System.out.println("go on");
        fileOutputStream.write(Arrays.copyOfRange(arr,2,arr.length));
    }

    public byte[] endWorkFile(byte[] arr) throws IOException {
        //файл закончен
        System.out.println("end");
        fileOutputStream.close();
        return new byte[]{-124,-120};
    }
}
