package util;

import connection.Listener;
import connection.User;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dominic
 * @since 16-Oct-16
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
public class VoiceRecorder extends VoiceUtil {

    private static  String username;
    private static ArrayList<User> participants = null;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public static void captureAudio() {
        try {
            final AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int)format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    out = new ByteArrayOutputStream();
                    isRecording = true;
                    try {
                        while (isRecording) {
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) {
                                out.write(buffer, 0, count);
                            }
                        }
                    } finally {
                        try {
                            out.close();
                            out.flush();
                            line.close();
                            line.flush();
                            Listener.sendVoiceMessage(username, participants ,out.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " );
            e.printStackTrace();
        }
    }
}
