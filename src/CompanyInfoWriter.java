import java.io.Closeable;
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.OutputStream;
 
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook; 
 
public class CompanyInfoWriter implements Closeable { 
        
    private final Workbook wb = new HSSFWorkbook();
    
    private final Sheet sheet = wb.createSheet("企业信息"); 
    
    private int rowId = 1;
    
    public CompanyInfoWriter() {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("公司名称");
        row.createCell(1).setCellValue("电话");
        row.createCell(2).setCellValue("地址");
    }
    
    public void add(CompanyInfo info) {
        Row row = sheet.createRow(rowId++);
        row.createCell(0).setCellValue(info.getName());
        row.createCell(1).setCellValue(String.join(";", info.getTelePhones()));
        row.createCell(2).setCellValue(info.getAddress());
    }
    
    public void save(String fileName) throws IOException { 
        //创建一个文件 命名为workbook.xls 
        try(FileOutputStream fileOut = new FileOutputStream(fileName)){
            // 把上面创建的工作簿输出到文件中 
            wb.write(fileOut); 
        }
    } 

    @Override
    public void close() throws IOException {
        wb.close();
    }
} 