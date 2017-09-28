
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {

    private String url;

    private Map<String, Object> params;

    HttpClient(String url, Map<String, Object> params) {
        this.url = url;
        this.params = params;
    }

    public String get() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建httpget.    
            String getUrl = buildGetUrl();
            System.out.println("get url:" + getUrl);
            HttpGet httpget = new HttpGet(getUrl);
            // 执行get请求.    
            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                // 获取响应实体    
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildGetUrl() {
        if (params == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        boolean isFirst = true;
        for (Entry<String, Object> e : params.entrySet()) {
            String value = String.valueOf(e.getValue());
            if(isFirst) {
                sb.append('?');
                isFirst = false;
            } else {
                sb.append('&');
            }
            try {
                sb.append(e.getKey()).append('=').append(URLEncoder.encode(value, "utf-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return sb.toString();
    }
}
