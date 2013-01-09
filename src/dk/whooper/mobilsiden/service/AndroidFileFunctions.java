package dk.whooper.mobilsiden.service;

import android.content.Context;

import java.io.*;

class AndroidFileFunctions {


    public static String getFileValue(String fileName, Context context) {
        try {

            StringBuilder outStringBuf = new StringBuilder();
            String inputLine;

        /*
         * We have to use the openFileInput()-method the ActivityContext
         * provides. Again for security reasons with openFileInput(...)
         */
            FileInputStream fIn = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader inBuff = new BufferedReader(isr);

            while ((inputLine = inBuff.readLine()) != null) {
                outStringBuf.append(inputLine);
                outStringBuf.append("\n");
            }

            inBuff.close();

            return outStringBuf.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean setFileValue(String fileName, String value, Context context) {
        return writeToFile(fileName, value, context);
    }

    public static boolean writeToFile(String fileName, String value, Context context) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return true;
    }

    public static void deleteFile(String fileName, Context context) {
        try {
            context.deleteFile(fileName);
        } catch (NullPointerException e) {
            //File doesn't exist - Do nothing
        }
    }
}