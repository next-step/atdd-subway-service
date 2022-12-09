package nextstep.subway.line.application;

public class AgeChargeCalculator {
    public static int calculate(int charge, int age) {

        if(age >= 6 && age < 13){
            return (charge - 350) * 50 / 100;
        }

        if(age >= 13 && age < 19){
            return (charge - 350) * 80 / 100;
        }
        if(age < 6 || age >= 65){
            return 0;
        }
        return charge;
    }
}
