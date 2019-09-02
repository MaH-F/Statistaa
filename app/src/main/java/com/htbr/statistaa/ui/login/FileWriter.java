package com.htbr.statistaa.ui.login;


import android.content.Context;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileWriter {


    public static void writeBytesToFile(Context context, String fileName, byte[] data){
        String filename = fileName;
        FileOutputStream outputStream;


        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeToFile(Context context, String fileName, String data){
        String filename = fileName;
        String fileContents = data;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void writeNewToFile(Context context, String fileName, String data){
        String filename = fileName;
        String fileContents = data;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);



            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }


        }
        catch (IOException e) {
            e.printStackTrace();
            sb.append("{}");
            //sb.append("File does not exist");
            //append separator
           // sb.append(";");
        }
        // context.deleteFile(fileName);
        return sb.toString();
    }

//    public void writeToExternalFile(Context context,String fileName, String data){
//        if(isExternalStorageWritable()) {
//
//            String filename = fileName;
//            String fileContents = data;
//            FileOutputStream outputStream;
//
//            try {
//                outputStream = context.openFileOutput(getPublicDocumentStorageDir("").getPath(), Context.MODE_APPEND);
//                outputStream.write(fileContents.getBytes());
//                outputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public File getPublicDocumentStorageDir(String albumName) {
        // Get the directory for the user's public documents directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.e("HTBR" ,"Directory not created");
        }
        return file;
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public void delete(Context context, String fileName){
        context.deleteFile(fileName);
    }

}