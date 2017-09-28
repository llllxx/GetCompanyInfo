
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
import org.json.JSONException;

public class GetCompanyInfoBaidu implements GetCompanyInfo {

    static final int maxPageNum = 20;

    static final String url = "http://api.map.baidu.com/place/v2/search";

    static final String apiKey = "7XHowjUI1wjI7rt5Sc1RmuF3fstdLAvD";
    
    static final  int SUCCESS = 0;

    @Override
    public List<CompanyInfo> query(CompanyQuery query) {
        List<CompanyInfo> list = new LinkedList();
        Map<String, Object> params = new HashMap();
        params.put("query", query.getKeyword());
        params.put("page_size", maxPageNum);
        params.put("region", query.getCity());
        params.put("ak", apiKey);
        params.put("output", "json");
        for (int pageNum = 0;; pageNum++) {
            params.put("page_num", pageNum);
            String str = new HttpClient(url, params).get();
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            String message = obj.getString("message");
            if (status != SUCCESS) {
                throw new GetCompanyInfoException(message);
            }
            int total = obj.getInt("total");
            JSONArray array = obj.getJSONArray("results");
            for (Object o : array) {
                JSONObject element = (JSONObject) o;
                CompanyInfo info = new CompanyInfo();
                info.setName(element.getString("name"));
                info.setTelePhones(Arrays.asList(element.optString("telephone").split(";")));
                info.setAddress(element.optString("address"));
                list.add(info);
            }
            if (total < maxPageNum) {
                break;
            }
        }
        return list;
    }

}
