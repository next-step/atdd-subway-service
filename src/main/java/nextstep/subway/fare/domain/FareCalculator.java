package nextstep.subway.fare.domain;

import java.util.Comparator;
import java.util.List;
import nextstep.subway.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import org.springframework.http.HttpStatus;

public class FareCalculator {

    private final int distance;
    private final List<SectionEdge> sectionEdges;

    public FareCalculator(Path path) {
        this.distance = path.getDistance();
        this.sectionEdges = path.getEdges();
    }
    public Fare calculate(Integer age) {
        Fare fare = DistanceFarePolicy.calculate(this.distance);
        if(age != null) {
            fare = DiscountFarePolicy.calculate(fare, age);
        }
        return fare.plus(getMaxAddedFare());
    }

    public Fare getMaxAddedFare() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .map(Line::getExtraFare)
                .max(Comparator.comparing(Fare::value))
                .orElseThrow(() -> new SubwayException(HttpStatus.NOT_FOUND,"추가요금을 찾을 수 없습니다"));
    }

    public int getDistance() {
        return distance;
    }
}
