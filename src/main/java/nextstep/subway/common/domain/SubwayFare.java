package nextstep.subway.common.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.StationGraphPath;
import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SubwayFare implements FareCaculator{
    public static final BigDecimal BASIC_FARE = BigDecimal.valueOf(1250);
    private BigDecimal subwayFare = BASIC_FARE;

    public SubwayFare() {
    }

    public SubwayFare(BigDecimal subwayFare) {
        this.subwayFare = subwayFare.setScale(0, RoundingMode.HALF_UP);
    }


    public SubwayFare calculate(List<Section> allSectionList, StationGraphPath stationGraphPath, Integer age) {
        calculateLineSurcharge(allSectionList, stationGraphPath.getPathStations());
        subwayFare = SurchargeByDistance.charge(subwayFare, stationGraphPath.getDistance());
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
}
