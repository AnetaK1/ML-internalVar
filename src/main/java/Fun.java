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
}
