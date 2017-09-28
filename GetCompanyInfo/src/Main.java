public class Main {
    
    static String[] keywords = {
        "购物", "生活服务", "丽人", "旅游景点", "休闲娱乐", "运动健身"
    };
    
    public static void main(String[] args) throws Exception {
        for (String keyword : keywords) {
            GetCompanyInfo getInfo = new GetCompanyInfo(keyword);
        }
    }
}