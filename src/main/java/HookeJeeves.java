import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HookeJeeves {
    private Fun fun;

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

        // data.saveData(stepsValue,vectorValues,fName);
        return xB;
    }
}
