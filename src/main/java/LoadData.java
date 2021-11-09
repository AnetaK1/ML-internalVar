import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadData {

    final Double Kelvin = 273.15;
    private List<Double> temp;
    private List<Double> eDot;
    private List<List<Double>> sigmaExp;
    private List<List<Double>> eps;

    public LoadData() {
        temp = new ArrayList<>();
        temp.add(475.0);
        eDot = new ArrayList<>();
        eDot.add(0.1);
        eDot.add(1.0);
        sigmaExp = new ArrayList<>();

//        List<Double> s1 = new ArrayList<>();
//        s1.addAll(Arrays.asList(26.9136, 60.4938, 79.5062, 89.3827, 97.284, 103.7037, 106.9136, 105.9259, 95.8025, 86.1728, 85.1852, 84.4444, 83.9506, 85.1852));
//
//        List<Double> s2 = new ArrayList<>();
//        s2.addAll(Arrays.asList(29.3103, 65.5172, 84.4828, 97.5862, 106.8966, 113.4483, 120.6897, 124.4828, 127.5862, 129.3103, 130.3448, 129.3103, 128.2759, 123.7931, 119.3103, 117.931));
//
//        sigmaExp.add(s1);
//        sigmaExp.add(s2);
//
//        List<Double> e1 = new ArrayList<>();
//        e1.addAll(Arrays.asList(0.0108, 0.0506, 0.0903, 0.13, 0.1707, 0.2104, 0.2501, 0.2907, 0.3603, 0.4406, 0.53, 0.6005, 0.7007, 0.7801
//        ));
//        List<Double> e2 = new ArrayList<>();
////        e2.addAll(Arrays.asList(0.0101, 0.0507, 0.0903, 0.1309, 0.1705, 0.2101, 0.2608, 0.3005, 0.3401, 0.3806, 0.4203, 0.4608, 0.5005, 0.5705, 0.671, 0.7604));
////        eps.add(e1);
////        eps.add(e2);

    }

    public Double getKelvin() {
        return Kelvin;
    }

    public List<Double> getTemp() {
        return temp;
    }

    public void setTemp(List<Double> temp) {
        this.temp = temp;
    }

    public List<Double> geteDot() {
        return eDot;
    }

    public void seteDot(List<Double> eDot) {
        this.eDot = eDot;
    }

    public List<List<Double>> getSigmaExp() {
        return sigmaExp;
    }

    public void setSigmaExp(List<List<Double>> sigmaExp) {
        this.sigmaExp = sigmaExp;
    }

    public List<List<Double>> getEps() {
        return eps;
    }

    public void setEps(List<List<Double>> eps) {
        this.eps = eps;
    }
}
