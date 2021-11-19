import java.util.ArrayList;
import java.util.List;

public class Particle {
    List<Double> x;
    List<Double> v;


    Particle(List<Double> x, List<Double> v) {
        this.x = x;
        this.v = v;
    }

    Particle() {
        x = new ArrayList<>();
        v = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            v.add(0.0);
            x.add(0.0);
        }
    }

    public List<Double> getX() {
        return x;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public List<Double> getV() {
        return v;
    }

    public void setV(List<Double> v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + (x) +
                '}';
    }
}

