package ca.polymtl.inf8480.tp1.shared;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// Reference: https://www.programcreek.com/java-api-examples/java.security.DigestInputStream
public class Util
{
    public static String getChecksum(File file) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        String checksum = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(inputStream, md);
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) > -1)
            {

            }
            MessageDigest digest = dis.getMessageDigest();
            dis.close();
            byte[] md5 = digest.digest();

            // Convert each byte to hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }

            checksum = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        
        return checksum;
    }
}