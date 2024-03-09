package com.aki.mcutils.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DebugDataWriter {
    public String DataHolder = "";
    public String DataPath = "";

    public LinkedList<String> WriteWaiting = new LinkedList<>();

    //public PrintWriter printWriter = null;

    public void StartWriting(String holder, String name) {
        String Days = getDays();

        this.DataHolder = holder;
        this.DataPath = holder + name.replace("#", ""/*"-" + Days*/);//データの容量が大きくなりすぎる可能性があるため削除

        //if(printWriter == null) this.Writer();
        WriteData("(By MCUtils) Data Logging Start: " + getTimeData());
    }

    public String getDays() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Days = sdf.format(cal.getTime());
        return Days;
    }

    public void EndWriting() {
        /*if(printWriter != null) {
            this.printWriter.close();
        }*/
        this.WriteWaitingData();
    }

    public void WriteDataAndTime(String data) {
        /*if(printWriter == null) this.Writer();

        this.printWriter.write(getTimeData() + " : " + data + "\n");
        this.printWriter.flush();*/
        this.WriteWaiting.add(getTimeData() + " : " + data + "\n");
    }

    public String getTimeData() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return "(" + dtf1.format(localDateTime) + ")";
    }

    public void WriteData(String data) {
        this.WriteWaiting.add(data + "\n");
    }

    public void WriteWaitingData() {
        WriteDataThread writeDataThread = new WriteDataThread(this.WriteWaiting);
        writeDataThread.start();

        //this.WriteWaiting.clear();
        /*try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(DataPath)))) {
            for(String s : this.WriteWaiting) {
                pw.write(s);
                pw.flush();
            }
        } catch (IOException e) {
            Minecraft.getMinecraft().crashed(new CrashReport("MCUtils Write Error: " + e.getLocalizedMessage(), e));
            // 例外
        }*/
    }

    public class WriteDataThread extends Thread {
        public List<String> WriteWaiting = new ArrayList<>();

        public WriteDataThread(List<String> data) {
            this.WriteWaiting = data;
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            synchronized (DataPath) {
                try {
                    pw = new PrintWriter(new BufferedWriter(new FileWriter(DataPath, false)));
                    for (String s : this.WriteWaiting) {
                        pw.write(s);
                        pw.flush();
                    }
                } catch (IOException e) {
                    Minecraft.getMinecraft().crashed(new CrashReport("MCUtils Write Error: " + e.getLocalizedMessage(), e));
                    this.stop();
                    // 例外
                } finally {
                    Objects.requireNonNull(pw).close();
                }
            }
        }
    }
}
