import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HookeJeeves {
    private Fun fun;
    private Data data;

    public HookeJeeves() {
        fun = new Fun();
    }

    public Fun getFun() {
        return fun;
    }

    public List<Double> trialStage(List<Double> x, double s, String fname) {


        for (int j = 0; j < x.size(); j++) {

            List<Double> xNew = new ArrayList<>(x);
            xNew.set(j, x.get(j) + s);

            if (fun.chooseFun(fname, xNew) < fun.chooseFun(fname, x)) {

                x = new ArrayList<>(xNew);

            } else {

                List<Double> xTemp = new ArrayList<>(x);
                xTemp.set(j, x.get(j) - s);

                if (fun.chooseFun(fname, xTemp) < fun.chooseFun(fname, x)) {
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

            stepsValue.add(fun.chooseFun(fName, x));
            vectorValues.add(x);

            //   System.out.println(x);
            // System.out.println(fun.chooseFun(fName,x));
            xB = new ArrayList<>(x);
            x = new ArrayList<>(trialStage(xB, step, fName));

            if (fun.chooseFun(fName, x) < fun.chooseFun(fName, xB)) {

                while (fun.chooseFun(fName, x) < fun.chooseFun(fName, xB)) {


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
        List<Double> fin = hookeJeeves.HookeJeevesMethod("www",a,0.5,0.5,0.01);


    }
}
