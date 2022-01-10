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

public class interVar {

    // LoadData loadData;

    public final double Q = 238000;
    public final double R = 8.314;
    public final double b = 0.25 * Math.pow(10, -9);
    public final double u = 45000;
    public final double D = 30;
    int tInf = 0;

    List<List<Double>> time;
    List<List<Double>> ro;
    List<Double> temp;
    List<Double> eDot;
    final Double Kelvin = 273.15;
    private List<List<Double>> sigmaExp;
    private List<List<Double>> eps;
    List<List<Double>> roO;
    List<Double> saveT;

    List<Double> roCr;
    double roInt;
    List<List<Double>> A;
    final double n = 6;
    List<Double> m = new ArrayList<>();

    interVar() {
        temp = new ArrayList<>();
        eDot = new ArrayList<>();
        sigmaExp = new ArrayList<>();

        List<Double> s1 = new ArrayList<>();
        List<Double> s2 = new ArrayList<>();
        List<Double> e1 = new ArrayList<>();
        List<Double> e2 = new ArrayList<>();


        roCr = new ArrayList<>();
        A = new ArrayList<List<Double>>();
        roO = new ArrayList<List<Double>>();
        saveT = new ArrayList<>();
        roInt = Math.pow(10, 4);

        time = new ArrayList<>();
        ro = new ArrayList<>();
    }

    private void loadData(String fileName) throws IOException {

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

            List<Double> timeSingle = new ArrayList<>();
            List<Double> roSingle = new ArrayList<>();

            Row r = rowIt.next();
            Iterator<Cell> cellIteratorr = r.cellIterator();
            Cell c1 = cellIteratorr.next();
            Cell c2 = cellIteratorr.next();

            System.out.println(c1 + "           " + c2);

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
                    System.out.println(cell);


                }

                p++;
                timeSingle.add(es.get(0));
                roSingle.add(es.get(1));


            }

            time.add(timeSingle);
            ro.add(roSingle);
            m.add((double) p);
            p = 0;


        }
        fis.close();
    }

    public static double generateNumber(double min, double max) {

        return min + Math.random() * (max - min);
    }

    public static List<Double> generateSmalla() {
        List<Double> a = new ArrayList<>();
        a.add(generateNumber(0.01, 100) * Math.pow(10, -3));//1
        a.add(generateNumber(0.001, 1000));//2
        a.add(generateNumber(0.1, 100) * Math.pow(10, 3));//3
        a.add(generateNumber(Math.pow(10, -10), 1) * 3 * Math.pow(10, 10));//4
        a.add(generateNumber(0.01, 100) * Math.pow(10, 3));//5
        a.add(generateNumber(Math.pow(10, -4), 100));//6
        a.add(generateNumber(Math.pow(10, -3), 100));//7
        a.add(1.0);
        //a.add(generateNumber(Math.pow(10, -5), 10));//8
        a.add(0.0);//9
      //  a.add(generateNumber(Math.pow(10, -10), 10));//9
        a.add(generateNumber(Math.pow(10, -5), 10));//10
        a.add(0.0);//11
        a.add(generateNumber(Math.pow(10, -10), 10) * Math.pow(10, 13));//12
        a.add(generateNumber(Math.pow(10, -5), 10));//13

        //tu probowałam jako poczatkowe "a" dac te z pdfa mozesz sobie puscic
//                List<Double> apdf = new ArrayList<>();
//        apdf.add(1.4301 * Math.pow(10, -3));
//        apdf.add(98.439);
//        apdf.add(15.527 * Math.pow(10, 3));
//        apdf.add(0.000179 * 3 * Math.pow(10, 10));
//        apdf.add(154.88 * Math.pow(10, 3));
//        apdf.add(0.80704);
//        apdf.add(3.1946);
//        apdf.add(1.0);
//        apdf.add(0.0);
//        apdf.add(0.2899);
//        apdf.add(0.0);
//        apdf.add(0.000538 * Math.pow(10, 13));
//        apdf.add(0.15551);
        return a;
    }


    public double getN() {
        return n;
    }

    public int getM() {
        return ro.get(0).size();
    }

    public List<List<Double>> getTime() {
        return time;
    }

    public List<List<Double>> getRo() {
        return ro;
    }

    public List<Double> getTemp() {
        return temp;
    }

    public List<Double> geteDot() {
        return eDot;
    }

    public Double getKelvin() {
        return Kelvin;
    }


    public double countRoCr(List<Double> a, double Z) {
        double roCr = -a.get(8) + a.get(9) * Math.pow(Z, a.get(7));
        return roCr;
    }

    public double countZ(double T, double eDot) {
        double Z = eDot * Math.exp(Q / (R * T));
        return Z;
    }

    public List<Double> evalBigA(List<Double> smalla, double Z, double eDot, double T) {
        List<Double> A = new ArrayList<>();
        A.add(1 / (b * evall(smalla, Z)));
        A.add(smalla.get(1) * Math.pow(eDot, -smalla.get(6)) * Math.exp((-smalla.get(2)) / (R * T)));
        A.add(smalla.get(3) * (evalTau() / D) * Math.exp((-smalla.get(4) / (R * T))));
        return A;
    }

    public double evall(List<Double> a, double Z) {
        double l = a.get(0) / (Math.pow(Z, a.get(10)));
        return l;
    }

    public double evalTau() {
        double Tau = (Math.pow(10, 6) * u * Math.pow(b, 2)) / 2;
        return Tau;
    }

    public double evalRo(List<Double> ro, List<Double> A, double eDot, int k, double a8, int tInf, int j) {
        double result = A.get(0) * eDot -
                A.get(1) * ro.get(k) * eDot -
                A.get(2) * Math.pow(ro.get(k), a8) * tInf * ro.get((k - j));
        return result;
    }

    public double fGoalInterVar(List<Double> a) {
        double sum = 0.0;
        double n = getN();


        double roP = 0.0;

        for (int i = 0; i < n; i++) {
            double t = time.get(i).get(time.get(i).size() - 1);
            double m = this.m.get(i);
            double deltaT = t / m;


            double Z = countZ(temp.get(i), eDot.get(i));
            double roCrr = countRoCr(a, Z);


            roCr.add(roCrr);
            List<Double> Aa = evalBigA(a, Z, eDot.get(i), temp.get(i));
            A.add(Aa);

            double tt = 0.0;
            int k = 0;
            int tInf = 0;
            List<Double> roONew = new ArrayList<>();
            roONew.add(roInt);
//
//            System.out.println(ro.get(i));
            for (int j = 1; j < m; j++) {


                if (k == 0) {
                    if (roONew.get(j - 1) >= roCrr) {
                        k = j;
                        tInf = 1;
                    } else {
                        tInf = 0;
                    }
                }


                double roNew = roONew.get(j - 1) + deltaT * evalRo(ro.get(i), Aa, eDot.get(i), j, a.get(5), tInf, k);

                roONew.add(roNew);
                roP = getRo().get(i).get(j);
                sum += Math.pow((roP - roONew.get(j - 1)) / (roP), 2);
                tt += deltaT;


            }
            sum /= m;


        }

        sum /= n;
        return sum;
    }

    double chooseFun(String fNAme, List<Double> x) {

        double res = 0;
        switch (fNAme) {
            case "Var":
                res = fGoalInterVar(x);
                break;
            default:
                System.out.println("There is no such name of function!");
                res = 0;
        }

        return res;
    }


    public List<Double> trialStage(List<Double> x, double s, String fname) {


        for (int j = 0; j < x.size(); j++) {

            List<Double> xNew = new ArrayList<>(x);
            xNew.set(j, x.get(j) + s);

            if (chooseFun(fname, xNew) < chooseFun(fname, x)) {

                x = new ArrayList<>(xNew);

            } else {

                List<Double> xTemp = new ArrayList<>(x);
                xTemp.set(j, x.get(j) - s);

                if (chooseFun(fname, xTemp) < chooseFun(fname, x)) {
                    x = new ArrayList<>(xTemp);
                }
            }
        }
        return x;
    }


    public List<Double> HookeJeevesMethod(String fName, List<Double> x, double step, double alpha, double epsilon) throws IOException {


        List<Double> xB = new ArrayList<>();

        int o = 0;
        while (step > epsilon || o >= 100) {


            xB = new ArrayList<>(x);
            x = new ArrayList<>(trialStage(xB, step, fName));

            double funB = chooseFun(fName, xB);

            if (chooseFun(fName, x) < funB) {

                System.out.println(x);

                while (chooseFun(fName, x) < chooseFun(fName, xB)) {


                    o++;
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

            o++;
        }

        return xB;
    }

    public static void main(String[] args) throws IOException {
        interVar interVar = new interVar();

        double step = 0.5;
        double alpha = 0.5;
        double epsilon = 0.000001;
        interVar.loadData("dane.xlsx");
        List<Double> a = generateSmalla();
        List<Double> aa = interVar.HookeJeevesMethod("Var", a, step, alpha, epsilon);
        System.out.println(aa);
    }

}
