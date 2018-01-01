package util;

import connection.Listener;
import connection.User;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class CallRecorder extends CallUtil {
    private static String username;
    private static ArrayList<User> participants = null;

    public static void captureAudio() {
        try {

            Runnable runner = new Runnable() {
                public void run() {
                    out = new ByteArrayOutputStream();
                    isCalling = true;
                    try {
                        while (isCalling) {

                            final AudioFormat format = getAudioFormat();
                            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

                            line.open(format);
                            line.start();
                            int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                            byte buffer[] = new byte[bufferSize];
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) {
                               // out.write(buffer, 0, count);
                                DatagramPacket data = new DatagramPacket(buffer, buffer.length);
                                out.close();
                                out.flush();
                                line.close();
                                line.flush();
                                Listener.sendVoiceCall(username, participants, data.getData());
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }
}
