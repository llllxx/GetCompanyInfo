
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    
    static final String[] keywords = {
        "养生", "沐足", "母婴", "喜茶", "奶茶"
    };
    
    static final String outputFileName = "企业信息%s.xls";
    
    static final Set<String> companyNameSet = new HashSet();
    
    public static void main(String[] args) throws Exception {
        
        try(CompanyInfoWriter writer = new CompanyInfoWriter()) {
            CompanyQuery query = new CompanyQuery();
            query.setCity("佛山");
            for(String keyword : keywords) {
                query.setKeyword(keyword);
                GetCompanyInfo getInfo = new GetCompanyInfoBaidu();
                List<CompanyInfo> infos = getInfo.query(query);
                System.out.println(infos.size());
                infos.forEach((CompanyInfo info) -> {
                    if(isMatchCondition(info)) {
                        writer.add(info);
                    }
                });
            }
            DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH-mm-ss");
            
            String currentFileName = String.format(outputFileName, format.format(new Date()));
            writer.save(currentFileName);
        }
    }

    private static boolean isMatchCondition(CompanyInfo info) {
        if(!companyNameSet.add(info.getName())) {
            return false;
        }
        for(String telephone : info.getTelePhones()) {
            int index = telephone.indexOf("-");
            if(index < 0) {
                index = telephone.indexOf(")");
            }
            if(index >= 0) {
                telephone = telephone.substring(index + 1);
                if(telephone.startsWith("8") || telephone.startsWith("2")) {
                    return true;
                }
            }
        }
        return false;
    }
}