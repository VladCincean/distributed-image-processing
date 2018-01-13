package util;

import java.io.*;

/**
 * Created by andrapop on 2018-01-13.
 */
public class BytesUtil {
    // toByteArray and toObject are taken from: http://tinyurl.com/69h8l7x
    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch(Exception e) {
            System.out.println(e);
        }
        finally {
            try {
                if (oos != null) {

                    oos.close();

                }
                if (bos != null) {
                    bos.close();
                }
            } catch(IOException e) {

            }
        }
        return bytes;
    }

    public Object toObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        }
        catch (Exception e) {
            System.out.println("I don't caaare about objects");


        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {

            }
        }
        return obj;
    }
}


