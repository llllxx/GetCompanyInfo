
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


public class Test {
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode("%E8%B4%AD%E7%89%A9", "utf-8"));
    }
}
