package nextstep.subway.common.domain;

import nextstep.subway.exception.IllegalFareException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.StationGraphPath;
import nextstep.subway.station.domain.Station;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Embeddable
public class SubwayFare{
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal BASIC_FARE = BigDecimal.valueOf(1250);
    private BigDecimal subwayFare = BASIC_FARE;

    public SubwayFare() {
    }

    public SubwayFare(BigDecimal subwayFare) {
        validateCheck(subwayFare);
        this.subwayFare = subwayFare.setScale(0, RoundingMode.HALF_UP);
    }

    public SubwayFare(int subwayFare) {
        validateCheck(BigDecimal.valueOf(subwayFare));
        this.subwayFare = BigDecimal.valueOf(subwayFare);
    }

    private void validateCheck(BigDecimal subwayFare) {
        if (subwayFare.compareTo(ZERO) < 0) {
            throw new IllegalFareException("지하철 요금은 마이너스가 될 수 없습니다.");
        }
    }


    public SubwayFare calculate(List<Section> allSectionList, StationGraphPath stationGraphPath, Integer age) {
        calculateByLineSurcharge(allSectionList, stationGraphPath.getPathStations());
        calculateByDistanceSurcharge(stationGraphPath.getDistance());
        calculateByAgeDiscount(age);

        return new SubwayFare(subwayFare);
    }

    private void calculateByAgeDiscount(Integer age) {
        FareCaculator ageCalculator = new ByAgeCalculator();
        this.subwayFare = ageCalculator.calculate(subwayFare, age);
    }

    private void calculateByDistanceSurcharge(int distance) {
        FareCaculator distanceCalculator = new ByDistanceCalculator();
        this.subwayFare = distanceCalculator.calculate(subwayFare, distance);
    }

    public void calculateByLineSurcharge(List<Section> allSectionList, List<Station> pathStations) {
        allSectionList.stream()
                .forEach(section -> usingLineSurcharge(section, pathStations));
    }

    private void usingLineSurcharge(Section section, List<Station> pathStations) {
        if (pathStations.contains(section.getUpStation()) && pathStations.contains(section.getDownStation())) {
            this.subwayFare = this.subwayFare.add(section.getLine().getSurcharge());
        }
    }

    public BigDecimal charged() {
        return subwayFare;
    }

}
