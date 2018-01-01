package util;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;

public class CallUtil {
    protected static boolean isCalling = false;
    static ByteArrayOutputStream out;


    public static void setCalling(boolean flag){
        isCalling = flag;
    }
    public static boolean isCall() {
        return isCalling;
    }


    /**
     * Defines an audio format
     */
    static AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        boolean signed = true;
        int channels = 2;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }
}
