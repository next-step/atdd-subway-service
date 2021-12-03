package nextstep.subway.path.domain;

public class AgeFarePolicy {

    private final AgeFareChain chain;

    public AgeFarePolicy() {
        this.chain = new AgeChildFareChain(new AgeYouthFareChain(new AgeAdultFareChain(new AgeDefaultFareChain(null))));
    }

    public int calculate(int age) {
        return chain.calculate(age);
    }
}
