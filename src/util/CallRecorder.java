package util;

import connection.Listener;
import connection.User;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CallRecorder extends CallUtil {
    private static String username;
    private static ArrayList<User> participants = null;

    public static void captureAudio() {
        try {
            final AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    out = new ByteArrayOutputStream();
                    isCalling = true;
                    try {
                        while (isCalling) {
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) {
                                out.write(buffer, 0, count);
                                out.close();
                                out.flush();
                                Listener.sendVoiceCall(username, participants, out.toByteArray());

                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    } finally {

                        // line.close();
                        // line.flush();
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: ");
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
