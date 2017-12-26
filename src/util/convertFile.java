package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;

public class convertFile {

    public byte[] convertImageToByte (File file){
        byte[] res = new byte[1024];
        try{
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            res=baos.toByteArray();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
