package com.aki.mcutils.APICore.DataUtils;

import com.aki.mcutils.MCUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataUtils {
    /**
     * PathName = フォルダ階層 + ファイルネーム
     * */
    public static DataOutputStream getDataOutput(String PathName) throws IOException {
        OutputStream outputstream = new FileOutputStream(PathName);
        try (DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputstream)))) {
            return dataoutputstream;
        } catch (FileNotFoundException e) {
            MCUtils.logger.error("Output: " + e.getMessage());
            return null;
        }
    }



    /**
     * PathName = フォルダ階層 + ファイルネーム
     * */
    public static DataInputStream getDataInput(String PathName) throws IOException {
        InputStream inputstream = new FileInputStream(PathName);
        try (DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputstream)))) {
            return datainputstream;
        } catch (FileNotFoundException e) {
            MCUtils.logger.error("Input: " + e.getMessage());
            return null;
        }
    }
}
