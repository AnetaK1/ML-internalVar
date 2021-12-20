import java.util.ArrayList;
import java.util.List;

public class Fun {
    double fTest(List<Double> x) {
        double x1 = x.get(0);
        double x2 = x.get(1);
        return 2.5 * (Math.pow(x1, 2) - (x2)) * (Math.pow(x1, 2) - (x2)) + Math.pow(1 - x1, 2);
    }

    double fRosenbrock(List<Double> x) {

        return 100 * (x.get(1) - Math.pow(x.get(0), 2)) * (x.get(1) - Math.pow(x.get(0), 2)) + Math.pow(1 - x.get(0), 2);
    }


    double fRastrigin(List<Double> x) {

        double res = 20 + Math.pow(x.get(0), 2) + Math.pow(x.get(1), 2) - 10 * (Math.cos(2 * Math.PI * x.get(0)) + Math.cos(2 * Math.PI * x.get(1)));
        return res;
    }

    public double chooseFun(String fNAme, List<Double> x) {

        double res = 0;
        switch (fNAme) {
            case "FTest":
                res = fTest(x);
                break;
            case "Rosenbrock":
                res = fRosenbrock(x);
                break;
            case "Rastrigin":
                res = fRastrigin(x);
                break;
//            case "Var":
//                res = fGoalInterVar(x);
//                break;
            default:
                System.out.println("There is no such name of function!");
                res = 0;
        }

        return res;
    }

//    public double chooseFun(List<Double> x) {
//        double sum = 0.0;
//        double n = getN();
//
//        for (int i = 0; i < n; i++) {
//            double t = 1 / eDot.get(i);
//            double m = this.m.get(i);
//
//            double deltaT = t / m;
////
////            System.out.println("DEltaT: "+ deltaT);
////            System.out.println("NUMEr arkusza: "+ i);
////            System.out.println("A: "+a);
//
//            double Z = countZ(temp.get(i), eDot.get(i));
//            double roCrr = countRoCr(x, Z);
//            //   System.out.println("RoCR  "+roCrr+ "\nfor "+i);
//            roCr.add(roCrr);
//            List<Double> Aa = evalBigA(x, Z, eDot.get(i), temp.get(i));
//            A.add(Aa);
//            //    System.out.println("Big A:"+Aa);
//            double tt = 0.0;
//            int k = 0;
//            int tInf = 0;
//            List<Double> roONew = new ArrayList<>();
//            roONew.add(roInt);
//            for (int j = 0; j < m; j++) {
//
//                if (k == 0) {
//                    if (roONew.get(j) >= roCrr) {
//                        k = j;
//                        tInf = 1;
//                    } else {
//                        tInf = 0;
//                    }
//                }
//
//                //tt+=deltaT;
//                double roNew = roONew.get(j) + tt * evalRo(roONew, Aa, eDot.get(i), j, x.get(7), tInf, k);
//                roONew.add(roNew);
//
//
//
//                if (j != 0) {
//                    double sigma = getSigmaE().get(i).get(j);
//                    double sigmaO = (x.get(6) + x.get(5) * u * b * Math.sqrt(roONew.get(j - 1)));
//
//                    sum += Math.pow((sigma - sigmaO / (sigma)), 2);
//                }
//                tt += deltaT;
//            }
//            sum /= m;
//        }
//        sum /= n;
//        return sum;
//    }
}
