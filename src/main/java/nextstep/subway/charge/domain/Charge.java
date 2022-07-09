package nextstep.subway.charge.domain;

public class Charge {
    private int charge;

    public Charge(int charge) {
        this.charge = charge;
    }

    public void plus(int number) {
        charge += number;
    }

    public void minus(int number) {
        charge -= number;
    }

    public void multiply(double number) {
        charge *= number;
    }

    public int getChargeValue() {
        return charge;
    }
}
