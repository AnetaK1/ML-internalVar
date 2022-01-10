import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HookeJeeves {

    private Data data;

    double roInt;
    public final double Q = 198000;
    public final double Rr = 8.314;
    public final double b = 0.25 * Math.pow(10, -9);
    public final double u = 84600;
    public final double D = 30;
    int tInf = 0;

    //from file to internal variable
    List<List<Double>> eps;
    List<List<Double>> sigmaE;
    List<Double> temp;
    List<Double> eDot;
    final Double Kelvin = 273.15;

    List<Double> roCr;
    List<List<Double>> A;

    List<Double> m;

    public HookeJeeves() {
        data = new Data();
        roInt = Math.pow(10, 4);

        eps = new ArrayList<List<Double>>();
        sigmaE = new ArrayList<List<Double>>();
        temp = new ArrayList<>();
        eDot = new ArrayList<>();
        m = new ArrayList<>();
        try {
            loadData("dane.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }


        roCr = new ArrayList<>();
        A = new ArrayList<List<Double>>();
    }



    //load data from file 100 of rows
    public void loadData(String fileName) throws IOException {
        File excelFile = new File(fileName);
        FileInputStream fis = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        List<XSSFSheet> sheets = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.add(workbook.getSheetAt(i));
        }


        for (int i = 0; i < sheets.size(); i++) {
            Iterator<Row> rowIt = sheets.get(i).iterator();

            //  rowIt.next();

            List<Double> epsSingle = new ArrayList<>();
            List<Double> sigmaSingle = new ArrayList<>();

            Row r = rowIt.next();
            Iterator<Cell> cellIteratorr = r.cellIterator();
            Cell c1 = cellIteratorr.next();
            Cell c2 = cellIteratorr.next();

            //  System.out.println(c1 + "           " + c2);

            temp.add(Double.valueOf(String.valueOf(c1)) + Kelvin);
            eDot.add(Double.valueOf(String.valueOf(c2)));


            rowIt.next();
            int p = 0;
            while (rowIt.hasNext()) {


                Row row = rowIt.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<Double> es = new ArrayList<>();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    es.add(Double.valueOf(String.valueOf(cell)));
                    // System.out.println(cell);


                }

                p++;
                epsSingle.add(es.get(0));
                sigmaSingle.add(es.get(1));


            }

            eps.add(epsSingle);
            sigmaE.add(sigmaSingle);
            m.add((double) p);
            p = 0;


        }
        workbook.close();
        fis.close();
    }

    //number of sheets
    public int getN() {
        return sigmaE.size();
    }


    //evaluate ro critical
    public double countRoCr(List<Double> a, double Z) {
        double roCr = -a.get(10) + a.get(11) * Math.pow(Z, a.get(9));
        return roCr;
    }

    //evaluate Z
    public double countZ(double T, double eDot) {
        double Z = eDot * Math.exp(Q / (Rr * T));
        return Z;
    }

    //evaluate vector of three big A
    public List<Double> evalBigA(List<Double> smalla, double Z, double eDot, double T) {
        List<Double> A = new ArrayList<>();
        A.add(1 / (b * evall(smalla, Z)));
        A.add(smalla.get(1) * Math.pow(eDot, -smalla.get(8)) * Math.exp((-smalla.get(2)) / (Rr * T)));
        A.add(smalla.get(3) * (evalTau() / D) * Math.exp((-smalla.get(4) / (Rr * T))));
        return A;
    }

    //evaluate l
    public double evall(List<Double> a, double Z) {
        double l = a.get(0) / (Math.pow(Z, a.get(12)));
        return l;
    }

    //evaluate tau
    public double evalTau() {
        double Tau = (Math.pow(10, 6) * u * Math.pow(b, 2)) / 2;
        return Tau;
    }

    //eval ro in one step
    public double evalRo(List<Double> ro, List<Double> A, double eDot, int j, double a8, int tInf, int k) {
        double result = A.get(0) * eDot -
                A.get(1) * ro.get(j) * eDot -
                A.get(2) * Math.pow(ro.get(j), a8) * tInf * ro.get((j - k));
        return result;
    }

    public double chooseFun( List<Double> x) {

        double sum = 0.0;
        double n = getN();

        for (int i = 0; i < n; i++) {
            double t = 1 / eDot.get(i);
            double m = this.m.get(i);

            double deltaT = t / m;
//
//            System.out.println("DEltaT: "+ deltaT);
//            System.out.println("NUMEr arkusza: "+ i);
//            System.out.println("A: "+a);

            double Z = countZ(temp.get(i), eDot.get(i));
            double roCrr = countRoCr(x, Z);
            //   System.out.println("RoCR  "+roCrr+ "\nfor "+i);
            roCr.add(roCrr);
            List<Double> Aa = evalBigA(x, Z, eDot.get(i), temp.get(i));
            A.add(Aa);
            //    System.out.println("Big A:"+Aa);
            double tt = 0.0;
            int k = 0;
            int tInf = 0;
            List<Double> roONew = new ArrayList<>();
            roONew.add(roInt);
            for (int j = 0; j < m; j++) {

                if (k == 0) {
                    if (roONew.get(j) >= roCrr) {
                        k = j;
                        tInf = 1;
                    } else {
                        tInf = 0;
                    }
                }

                //tt+=deltaT;
                double roNew = roONew.get(j) + deltaT * evalRo(roONew, Aa, eDot.get(i), j, x.get(7), tInf, k);
                roONew.add(roNew);


                if (j != 0) {
                    double sigma = getSigmaE().get(i).get(j);
                    double sigmaO = (x.get(6) + x.get(5) * u * b * Math.sqrt(roONew.get(j)));

                    sum += Math.pow((sigma - sigmaO / (sigma)), 2);

                }
            }
            sum /= m;
        }
        sum /= n;
        return sum;
    }
//////////////////////////////////////////////////////////
    public List<Double> trialStage(List<Double> x, double s, String fname) {


        for (int j = 0; j < x.size(); j++) {

            List<Double> xNew = new ArrayList<>(x);
            xNew.set(j, x.get(j) + s);

            if (chooseFun(xNew) < chooseFun(x)) {

                x = new ArrayList<>(xNew);

            } else {

                List<Double> xTemp = new ArrayList<>(x);
                xTemp.set(j, x.get(j) - s);

                if (chooseFun(xTemp) < chooseFun(x)) {
                    x = new ArrayList<>(xTemp);
                }
            }
        }
        return x;
    }

    public List<Double> HookeJeevesMethod(String fName, List<Double> x, double step, double alpha, double epsilon) throws IOException {


        List<Double> xB = new ArrayList<>();
        List<Double> stepsValue = new ArrayList<>();
        List<List<Double>> vectorValues = new ArrayList<>();

        while (step > epsilon) {

            stepsValue.add(chooseFun(x));
            vectorValues.add(x);

              System.out.println(x);
              System.out.println(chooseFun(x));
            xB = new ArrayList<>(x);
            x = new ArrayList<>(trialStage(xB, step, fName));

            if (chooseFun(x) < chooseFun(xB)) {

                while (chooseFun(x) < chooseFun(xB)) {


                    List<Double> xBNew = new ArrayList<>(xB);
                    xB = new ArrayList<>(x);

                    for (int i = 0; i < x.size(); i++) {
                        double a = xB.get(i) * 2 - xBNew.get(i);
                        x.set(i, a);

                    }


                    x = new ArrayList<>(trialStage(x, step, fName));

                }
                x = new ArrayList<>(xB);

            } else {
                step *= alpha;
            }
        }

        return xB;
    }

    public List<List<Double>> getSigmaE() {
        return sigmaE;
    }

    //save ro based on small a vector
    public void save(List<Double> a) throws IOException {
        List<List<Double>> sigmaO = new ArrayList<>();
        List<List<Double>> timeO = new ArrayList<>();

        for (int o = 0; o < getN(); o++) {
            List<Double> ro = new ArrayList<>();
            List<Double> sigma = new ArrayList<>();

            double Z = countZ(temp.get(o), eDot.get(o));
            double roCr = countRoCr(a, Z);
            List<Double> A = evalBigA(a, Z, eDot.get(o), temp.get(o));
            ro.add(1000.0);
            double t = 1 / eDot.get(o);
            //double deltaT = t / this.m.get(o);
            double deltaT = 0.001;
            List<Double> timeS = new ArrayList<>();

            int k = 0;

            int tInf = 0;
            int j = 0;
           // System.out.println("Temp " + temp.get(o) + "    edot" + eDot.get(o) + "     dt" + t);
            for (double tt = 0; tt <= t; tt += deltaT) {
                timeS.add(tt);

                if (k == 0) {

                    if (ro.get(j) > roCr) {
                        k = j;

                        tInf = 1;

                    } else {
                        tInf = 0;

                    }
                }


                double roNew = ro.get(j) + deltaT * evalRo(ro, A, eDot.get(o), j, a.get(7), tInf, k);

                sigma.add(a.get(6) + a.get(5) * u * b * Math.sqrt(ro.get(j)));


                //System.out.println("tt: " + tt + "   sigma: " + a.get(6) + a.get(5) * u * b * Math.sqrt(ro.get(j)));
                ro.add(roNew);
                j++;

            }
            timeO.add(timeS);
            sigmaO.add(sigma);
        }

        data.saveData(a, getN(), this.m, sigmaO, sigmaE, eps);
        data.saveSigma(getN(), this.m, sigmaO, timeO);


    }
//    public void save(List<Double> a) throws IOException {
//        List<List<Double>> sigmaO = new ArrayList<>();
//
//        for (int o = 0; o < getN(); o++) {
//            List<Double> ro = new ArrayList<>();
//            List<Double> sigma = new ArrayList<>();
//
//            double Z = countZ(temp.get(o), eDot.get(o));
//            double roCr = countRoCr(a, Z);
//            List<Double> A = evalBigA(a, Z, eDot.get(o), temp.get(o));
//            double tCr = Math.pow(10, 8);
//            ro.add(roInt);
//            double t = 1 / eDot.get(o);
//            double deltaT = t / this.m.get(o);
//
//            int k = 0;
//            int j = 0;
//            int tInf = 0;
//
//            for (double i = 0; i < m.get(o); i ++) {
//
//                if (j == 0) {
//
//                    if (ro.get(k) >= roCr) {
//                        j = k;
//
//                        tInf = 1;
//
//                    } else {
//                        tInf = 0;
//
//                    }
//                }
//
//
//                double roNew = ro.get(k) + deltaT * evalRo(ro, A, eDot.get(o), k, a.get(7), tInf, j);
//                sigma.add(a.get(6) + a.get(5) * u * b * Math.sqrt(roNew));
//
//                ro.add(roNew);
//                k++;
//            }
//            System.out.println(ro);
//            sigmaO.add(ro);
//        }
//
//        data.saveData(a, getN(), this.m, sigmaO, sigmaE, eps);
//
//
//    }

    public static void main(String[] args) throws IOException {
        List<Double> a = interVar.generateSmalla();

        HookeJeeves hookeJeeves = new HookeJeeves();
        List<Double> fin = hookeJeeves.HookeJeevesMethod("www", a, 0.5, 0.5, 0.01);

        hookeJeeves.save(fin);

    }
}
