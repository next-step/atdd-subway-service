package nextstep.subway.path.domain.fare;

public interface FareRule {

    Fare extraFare(int distance, int lineFare);

    Fare discount(int age);

}
