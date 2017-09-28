
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.json.JSONException;

public class GetCompanyInfo {
//add a comment
    static final int maxPageNum = 20;
    
    static final String urlFormat = "http://api.map.baidu.com/place/v2/search?query={keyword}&page_size=" + maxPageNum + "&page_num={pageNum}&bounds={bounds}&output=json&ak=7XHowjUI1wjI7rt5Sc1RmuF3fstdLAvD";

    Set<String> set = new HashSet();

    String url;

    private BufferedWriter bw;
            
    public GetCompanyInfo(String keyword) throws IOException, WriteException {
        String keywordEncoded = URLEncoder.encode(keyword, "utf-8");
        url = urlFormat.replace("{keyword}", keywordEncoded);
        try {
            bw = new BufferedWriter(new FileWriter(keyword + ".txt"));
            for(double[] range : CoordinatesConfig.cordinatesRange) {
                query(range);
            }
        } finally {
            bw.close();
        }
        System.out.println("find keyword :" + keyword);
        System.out.println("find count:" + set.size());
    }

    private void output(String name, String telephone) throws IOException {
        bw.append(String.format("%s\t%s\n", name, telephone));
        System.out.printf("公司：%s, 电话：%s\n", name, telephone);
    }

    private void query(double[] coordinates)  {
        try {
            for (int pageNum = 0;; pageNum++) {
                String newUrl = url.replace("{pageNum}", String.valueOf(pageNum)).replace("{bounds}", buildBounds(coordinates));
                String str = new HttpClientTest().get(newUrl);

                JSONObject obj = new JSONObject(str);
                int status = obj.getInt("status");
                String message = obj.getString("message");
                if (status != 0) {
                    throw new IllegalStateException("status:" + status + ",message:" + message);
                }
                int total = obj.getInt("total");
                JSONArray array = obj.getJSONArray("results");
                for (Object o : array) {
                    JSONObject element = (JSONObject) o;
                    String uid = element.getString("uid");
                    String name = element.getString("name");
                    String telephone = element.optString("telephone");
                    if (matchCondition(telephone) && set.add(uid)) {
                        output(name, telephone);
                    }
                }
                if (total < maxPageNum) {
                    break;
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String buildBounds(double[] coordinates) {
        StringBuilder sb = new StringBuilder();
        for(double c : coordinates) {
            if(sb.length() > 0) {
                sb.append(',');
            }
            sb.append(String.format("%.3f", c));
        }
        return sb.toString();
    }

    static final String telRegionNum = "020";
    
    private boolean matchCondition(String telephone) {
        if(telephone.startsWith("(")) {
            telephone = telephone.substring(1);
        }
        if(!telephone.startsWith(telRegionNum)) {
            return false;
        }
        int index = telRegionNum.length();
        for(;; index++) {
            if(index >= telephone.length()) {
                return false;
            }
            char c = telephone.charAt(index);
            if(c >= '0' && c <= '9') {
                return c == '8' || c == '3';
            }
        }
    }

}
