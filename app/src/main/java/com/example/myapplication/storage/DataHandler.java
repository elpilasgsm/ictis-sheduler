package com.example.myapplication.storage;

import android.content.Context;
import android.widget.Toast;


import com.example.myapplication.model.SheduledDataStorage;

import java.io.*;

public class DataHandler {
    final private static String FILE_NAME = "TIMELINE_STORAGE";
    private static SheduledDataStorage serverCodeDTOS = null;

    private static SheduledDataStorage readFromFile(Context context, File file) throws FileNotFoundException, IOException {
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();
        return deserialize(fileData, SheduledDataStorage.class);
    }

    public static SheduledDataStorage getServerCodeDTOS(Context context) {
        if (serverCodeDTOS == null) {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                try {
                    serverCodeDTOS = readFromFile(context, file);
                } catch (Exception e) {
                    Toast.makeText(context, "Internal storage is corrupted!.", Toast.LENGTH_LONG).show();
                    return new SheduledDataStorage();
                }
            } else {
                serverCodeDTOS = new SheduledDataStorage();
            }
        }
        return serverCodeDTOS;
    }

    public static void updateData(Context context) {
        try {
            if (serverCodeDTOS == null) {
                serverCodeDTOS = new SheduledDataStorage();
                return;
            }
            final File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream f = new FileOutputStream(file);
            f.write(serialize(serverCodeDTOS));
            f.flush();
            f.close();
        } catch (Exception e) {
            Toast.makeText(context, "Error on saving data", Toast.LENGTH_LONG);
        }
    }


    private static byte[] serialize(Object storageDTO) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutput out;
        byte[] retVal = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(storageDTO);
            out.flush();
            retVal = bos.toByteArray();
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
        return retVal;
    }

    private static <T> T deserialize(byte[] data, Class<T> dataClass) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        T storage = null;
        try {
            in = new ObjectInputStream(bis);
            storage = dataClass.cast(in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return storage;
    }

}