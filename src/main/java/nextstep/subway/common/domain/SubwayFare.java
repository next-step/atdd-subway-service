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
    private static final BigDecimal MINIMUM = BigDecimal.ZERO;
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
        if (subwayFare.compareTo(MINIMUM) < 0) {
            throw new IllegalFareException("지하철 요금은 마이너스가 될 수 없습니다.");
        }
    }


    public SubwayFare calculate(List<Section> allSectionList, StationGraphPath stationGraphPath, Integer age) {
        calculateLineSurcharge(allSectionList, stationGraphPath.getPathStations());
//        subwayFare = SurchargeByDistance.charge(subwayFare, stationGraphPath.getDistance());
        subwayFare = DiscountByAge.discount(subwayFare, age);

        return new SubwayFare(subwayFare);
    }

    public void calculateLineSurcharge(List<Section> allSectionList, List<Station> pathStations) {
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

    public SubwayFare plus(BigDecimal addedFare) {
       return new SubwayFare(this.subwayFare.add(addedFare));
    }
}
