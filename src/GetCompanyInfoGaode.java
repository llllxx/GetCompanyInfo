
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;


public class GetCompanyInfoGaode implements GetCompanyInfo {

    static final String queryUrl = "http://restapi.amap.com/v3/place/text";
    
    static final String districtUrl = "http://restapi.amap.com/v3/config/district";
    
    static final int pageSize = 100;
    
    static final String apiKey = "57f86d2610eee4af34ec49067e0d2dd9";
    
    static final int SUCCESS = 1;
    
    @Override
    public List<CompanyInfo> query(CompanyQuery query) {
        List<CompanyInfo> list = new LinkedList();
        HashMap params = new HashMap();
        params.put("keywords", query.getKeyword());
        params.put("key", apiKey);
        params.put("offset", pageSize);
        for(String district : getSubDistricts(query.getCity())) {
            params.put("city", district);
            for(int page = 1; queryByPage(list, params, page); page++);
        }
        return list;
    }

    /**
     * @return 是否还有下一页
     */
    private boolean queryByPage(List<CompanyInfo> list, HashMap<String, Object> params, int page) {
        params.put("page", page);
        String result = new HttpClient(queryUrl, params).get();
        JSONObject json = new JSONObject(result);
        int status = json.getInt("status");
        if(status != SUCCESS) {
            throw new GetCompanyInfoException(json.getString("info"));
        }
        int count = json.getInt("count");
        JSONArray pois = json.getJSONArray("pois");
        pois.forEach((Object obj) ->{
            JSONObject poi = (JSONObject)obj;
            CompanyInfo info = new CompanyInfo();
            info.setName(poi.getString("name"));
            info.setAddress(poi.optString("address"));
            String tel = poi.optString("tel");
            List<String> telephones = new ArrayList();
            Collections.addAll(telephones, tel.split(";"));
            info.setTelePhones(telephones);
            list.add(info);
        });
        return page * pageSize < count;
    }

    private Iterable<String> getSubDistricts(String city) {
        HashMap params = new HashMap();
        params.put("keywords", city);
        params.put("key", apiKey);
        String result = new HttpClient(districtUrl, params).get();
        JSONObject json = new JSONObject(result);
        int status = json.getInt("status");
        if(status != SUCCESS) {
            throw new GetCompanyInfoException(json.getString("info"));
        }
        List<String> list = new LinkedList();
        JSONArray districts = json.getJSONArray("districts");
        districts.forEach((Object obj) ->{
            JSONObject district = (JSONObject)obj;
            JSONArray subDistricts = district.getJSONArray("districts");
            subDistricts.forEach((Object subObj) ->{
                JSONObject subDistrict = (JSONObject)subObj;
                list.add(subDistrict.getString("name"));
            });
        });
        return list;
    }
}
