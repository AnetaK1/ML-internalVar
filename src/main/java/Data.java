import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.List;

public class Data {


    public void savePSOData(List<Particle> G, List<Double> fGoal) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("ValueOfGoalFun.csv"));
        for (int i = 0; i < G.get(0).getX().size(); i++) {
            writer.append("x" + i);
            writer.append(";");
        }
        writer.append("Value of function");
        writer.append("\n");
        for (int i = 0; i < G.size(); i++) {
            for (int j = 0; j < G.get(i).getX().size(); j++) {
                writer.append(String.valueOf(G.get(i).getX().get(j)) + ";");
            }
            writer.append(fGoal.get(i).toString() + ";");
            writer.append("\n");


        }

        writer.close();
    }

    public void saveData(List<Double> a, int n, List<Double> m, List<List<Double>> sigmaO, List<List<Double>> sigmaE, List<List<Double>> eps) throws IOException {

        String excelFileName = "InternakVar.xls";

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = null;
        for (int i = 0; i < n; i++) {
            sheet = wb.createSheet("Arkusz" + i);
            double mm = m.get(i);


            for (int r = 0; r < mm; r++) {
                HSSFRow row = sheet.createRow(r);
                if (r == 0) {
                    for (int c = 0; c < 4; c++) {
                        HSSFCell cell = row.createCell(c);
                        switch (c) {
                            case 0:
                                cell.setCellValue("epsP");
                                break;
                            case 1:
                                cell.setCellValue("sigmaE");
                                break;
                            case 2:
                                cell.setCellValue("sigmaO");
                                break;
                            default:
                                cell.setCellValue(0);
                        }
                    }
                } else {


                    for (int c = 0; c < 4; c++) {
                        HSSFCell cell = row.createCell(c);

                        switch (c) {
                            case 0:
                                cell.setCellValue(eps.get(i).get(r));
                                break;
                            case 1:
                                cell.setCellValue(sigmaE.get(i).get(r));
                                break;
                            case 2:
                                cell.setCellValue(sigmaO.get(i).get(r));
                                break;
                            default:
                                cell.setCellValue(0);
                        }

                    }
                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();


    }
}

