package helper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Helper {
	public static String getCellContents(Cell cell) {
		switch (cell.getCellType())
        {
        	case Cell.CELL_TYPE_STRING:
        		return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
            	if (HSSFDateUtil.isCellDateFormatted(cell)) {
        			Date date = cell.getDateCellValue();
        			return new SimpleDateFormat("dd/MM/yyyy").format(date);
        		}
            	
                return Double.toString(cell.getNumericCellValue());
        }
		
		return null;
	}
	
	public static void writeWorkbook(Map<String, Object[]> data, String filename) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
        Set<String> keyset = data.keySet();
        int rownum = 0;
        
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
            }
        }
        try
        {
            FileOutputStream out = new FileOutputStream(new File(filename));
            workbook.write(out);
            out.close();
            workbook.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}
