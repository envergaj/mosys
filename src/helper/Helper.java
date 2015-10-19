package helper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
	
	public static Map<String, Object[]> removeEmptyColumns(Map<String, Object[]> rows,
			Set<Integer> filledColumns) {
		Map<String, Object[]> newRows = new TreeMap<String, Object[]>();
		
		for (Entry<String, Object[]> entry : rows.entrySet()) {
		    String key = entry.getKey();
		    Object[] value = entry.getValue();
		    ArrayList<Object> newValue = new ArrayList<Object>();
		    
		    for (int i = 0; i < value.length; i++) {
		    	if (filledColumns.contains(i)) {
		    		newValue.add(value[i]);
		    	}
		    }
		    
		    newRows.put(key, newValue.toArray(new Object[newValue.size()]));
		}
		
		return newRows;
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
	
	public static void deleteColumn(Sheet sheet, int columnToDelete) {
        int maxColumn = 0;
        for (int r=0; r < sheet.getLastRowNum()+1; r++) {
            Row row = sheet.getRow(r);
            
            if (row == null)
                continue;
            
            int lastColumn = row.getLastCellNum();
            if (lastColumn > maxColumn)
                maxColumn = lastColumn;

            if (lastColumn < columnToDelete)
                continue;

            for (int x=columnToDelete+1; x < lastColumn + 1; x++) {
                Cell oldCell    = row.getCell(x-1);
                if (oldCell != null)
                    row.removeCell(oldCell);

                Cell nextCell   = row.getCell(x);
                if (nextCell != null) {
                    Cell newCell    = row.createCell(x-1, nextCell.getCellType());
                    cloneCell(newCell, nextCell);
                }
            }
        }

        for (int c=0; c < maxColumn; c++) {
            sheet.setColumnWidth(c, sheet.getColumnWidth(c+1));
        }
    }

    private static void cloneCell(Cell cNew, Cell cOld) {
        cNew.setCellComment(cOld.getCellComment());
        cNew.setCellStyle(cOld.getCellStyle());

        switch (cNew.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN: {
                cNew.setCellValue(cOld.getBooleanCellValue());
                break;
            }
            case Cell.CELL_TYPE_NUMERIC: {
                cNew.setCellValue(cOld.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_STRING: {
                cNew.setCellValue(cOld.getStringCellValue());
                break;
            }
            case Cell.CELL_TYPE_ERROR: {
                cNew.setCellValue(cOld.getErrorCellValue());
                break;
            }
            case Cell.CELL_TYPE_FORMULA: {
                cNew.setCellFormula(cOld.getCellFormula());
                break;
            }
        }

    }
}
