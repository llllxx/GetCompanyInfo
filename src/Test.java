
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


public class Test {
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        System.out.println(format.format(new Date()));
        //System.out.println(URLDecoder.decode("%E4%BD%9B%E5%B1%B1%E5%B8%82", "utf-8"));
    }
}
