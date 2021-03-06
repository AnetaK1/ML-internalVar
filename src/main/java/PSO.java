import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class PSO {


    //to mozna modyfikowac ilosc krokow po ktorych pso się zatrzyma
    final int maxIter = 1000;

    //to save data
    private Data data;

    //PSO
    private List<Particle> R; //swarm
    private int amount; // amount of particle in swarm
    private int size; //dimension of particle
    Random rand = new Random();
    ////////////////////////////////////////
    //Internal Var
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

    //////////////////////////////////////////
    PSO() {
        data = new Data();
        R = new ArrayList<Particle>();
        amount = 15;
        size = 2;

        ///////////
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

    PSO(int amount, int size) {
        data = new Data();
        //init to PSO
        R = new ArrayList<Particle>();
        this.amount = amount;
        this.size = size;
        ///////////////////////////////
        //init to internal variable
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


    //amount of particle
    public int getAmount() {
        return amount;
    }

    //length of vector in particle
    public int getSize() {
        return size;
    }

    //iniialize swarm
    public void createR(List<List<Double>> x) {

        for (int i = 0; i < amount; i++) {
            List<Double> v = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                v.add(0.0);
            }
            R.add(new Particle(x.get(i), v));
        }
    }

    //generate new swarm
    public List<Particle> generateNewR(List<Particle> R, double w, double c1, double c2, Particle G, List<List<Double>> p) {
        List<Particle> newR = new ArrayList<>();

        int Rsize = R.size();

        for (int i = 0; i < Rsize; i++) {
            double r1 = rand.nextDouble();
            double r2 = rand.nextDouble();
            int vecLen = R.get(i).getV().size();

            List<Double> newV = new ArrayList<>();
            List<Double> newX = new ArrayList<>();

            for (int j = 0; j < vecLen; j++) {
                newV.add(w * R.get(i).getV().get(j) +
                        c1 * r1 * (p.get(i).get(j) - R.get(i).getX().get(j)) +
                        c2 * r2 * (G.getX().get(j) - R.get(i).getX().get(j)));

                newX.add(R.get(i).getX().get(j) + newV.get(j));
            }

            newR.add(new Particle(newX, newV));

        }


        return newR;
    }

    //calculate fun goal in PSO
    public List<Double> calcFunGoal(List<Particle> R) {
        List<Double> fGoal = new ArrayList<>();
        for (int i = 0; i < R.size(); i++) {
            fGoal.add(chooseFun(R.get(i).getX()));
        }
        return fGoal;
    }

    //find best position
    public List<List<Double>> bestPosition(List<Particle> R) {
        List<List<Double>> P = new ArrayList<List<Double>>();
        for (int i = 0; i < R.size(); i++) {
            P.add(R.get(i).getX());
        }

        return P;
    }

    //find best particle
    public Particle findBestParicle(List<Double> fGoal, List<Particle> R, Particle G) {
        int index = 0;
        Double min = Collections.min(fGoal);

        for (int i = 0; i < fGoal.size(); i++) {
            if (fGoal.get(i) == min) {
                index = i;
                break;
            }
        }

        if (chooseFun(G.getX()) > fGoal.get(index)) {
            return R.get(index);
        } else return G;


    }

    ////////////////////////////////////////////////
//internal variable methods
    /////////////////////////////////////
    //load data from file
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

            List<Double> epsSingle = new ArrayList<>();
            List<Double> sigmaSingle = new ArrayList<>();

            Row r = rowIt.next();
            Iterator<Cell> cellIteratorr = r.cellIterator();
            Cell c1 = cellIteratorr.next();
            Cell c2 = cellIteratorr.next();


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

    //definition of goal function in internal variable
    public double chooseFun(List<Double> x) {
        double sum = 0.0;
        double n = getN();

        for (int i = 0; i < n; i++) {
            double t = 1 / eDot.get(i);
            double m = this.m.get(i);

            //tu jest krok czsowy mozna probowac modyfikowac
            double deltaT = t / 1000;


            double Z = countZ(temp.get(i), eDot.get(i));
            double roCrr = countRoCr(x, Z);

            roCr.add(roCrr);
            List<Double> Aa = evalBigA(x, Z, eDot.get(i), temp.get(i));
            A.add(Aa);

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


              //  if (j != 0) {
                double sigma = getSigmaE().get(i).get(j);
                double sigmaO = (x.get(6) + x.get(5) * u * b * Math.sqrt(roONew.get(j)));

                sum += Math.pow((sigma - sigmaO / (sigma)), 2);

              //   }
            }
            sum /= m;
        }
        sum /= n;
        return sum;
    }

    //get vector of ro from file
    public List<List<Double>> getSigmaE() {
        return sigmaE;
    }

    //PSO
    public Particle methodPSO(double w, double c1, double c2, List<List<Double>> vect) {

        List<Particle> saveG = new ArrayList<>();
        List<Double> saveFGoal = new ArrayList<>();

        createR(vect);

        List<Double> fGoal = calcFunGoal(R);
        List<List<Double>> P = bestPosition(R);

        int index = 0;
        Double min = Collections.min(fGoal);


        for (int i = 0; i < fGoal.size(); i++) {
            if (fGoal.get(i) == min) {
                index = i;
                break;
            }
        }

        //best particle
        Particle G = R.get(index);
        int iter = 0;
        while (iter < maxIter) {
            iter++;
            R = generateNewR(R, w, c1, c2, G, P);
            fGoal = calcFunGoal(R);
            P = bestPosition(R);
            G = findBestParicle(fGoal, R, G);
            Double fActualValue = chooseFun(G.getX());
            //Stop if function value occurs more than 10 times
//            if (Collections.frequency(saveFGoal, fActualValue) >= 50) {
//                break;
//            }
            // data to file
            saveFGoal.add(fActualValue);
            saveG.add(G);
        }

        try {
            data.savePSOData(saveG, saveFGoal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return G;
    }
//////////////////////////////////


    //liczenie sigm w petli po czasie dla wynikowego wektora a
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
            System.out.println("Temp " + temp.get(o) + "    edot" + eDot.get(o) + "     dt" + t);
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
                ro.add(roNew);
                j++;

            }
            timeO.add(timeS);
            sigmaO.add(sigma);
        }

       // data.saveData(a, getN(), this.m, sigmaO, sigmaE, eps);
        data.saveSigma(getN(), this.m, sigmaO, timeO);


    }

    public static void main(String[] args) {
        List<Double> a = interVar.generateSmalla();
        //amount możesz modyfikować
        PSO pso = new PSO(1000, a.size());
        List<List<Double>> aa = new ArrayList<>();
        for (int i = 0; i < pso.getAmount(); i++) {
            aa.add(interVar.generateSmalla());
        }
        //w, c1,c2 mozesz modyfikowac
        double w = 0.5;
        double c1 = 1;
        double c2 = 1;
        Particle x = pso.methodPSO(w, c1, c2, aa);
          List<Double> fin = x.getX();
          //wektor a z pdfa
//        List<Double> apdf = new ArrayList<>();
//        apdf.add(1.4301 * Math.pow(10, -3));
//        apdf.add(98.439);
//        apdf.add(15.527 * Math.pow(10, 3));
//        apdf.add(0.000179 * 3 * Math.pow(10, 10));
//        apdf.add(154.88 * Math.pow(10, 3));
//        apdf.add(0.80704);
//        apdf.add(3.1946);
//        apdf.add(0.98381);
//        apdf.add(0.005912);
//        apdf.add(0.2899);
//        apdf.add(0.0);
//        apdf.add(0.000538 * Math.pow(10, 13));
//        apdf.add(0.15551);


        try {
            pso.save(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

