package nextstep.subway.common.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DiscountByAge;
import nextstep.subway.path.domain.SurchargeByDistance;
import nextstep.subway.path.dto.StationGraphPath;
import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SubwayFare {
    public static final BigDecimal BASIC_FARE = BigDecimal.valueOf(1250);
    private BigDecimal subwayFare = BASIC_FARE;

    public SubwayFare() {
    }

    public SubwayFare(BigDecimal subwayFare) {
        this.subwayFare = subwayFare;
    }

    public static BigDecimal chargeByDistance(int distance) {
        return SurchargeByDistance.charge(BASIC_FARE, distance);
    }

    public static SubwayFare calculateSubwayFare(List<Section> allSectionList, StationGraphPath stationGraphPath, Integer age) {
        BigDecimal chargedFare = calculateLineSurcharge(allSectionList, stationGraphPath.getPathStations());
        chargedFare = chargedFare.add(chargeByDistance(stationGraphPath.getDistance()));
        chargedFare = DiscountByAge.discount(chargedFare, age);
        return new SubwayFare(chargedFare.setScale(0, RoundingMode.HALF_UP));
    }

    public static BigDecimal calculateLineSurcharge(List<Section> allSectionList, List<Station> pathStations) {
        BigDecimal lineSurchargedFare = BigDecimal.ZERO;
        for (Section section : allSectionList) {
            lineSurchargedFare = lineSurchargedFare.add(usingLineSurcharge(section, pathStations));
        }
        return lineSurchargedFare;
    }

    private static BigDecimal usingLineSurcharge(Section section, List<Station> pathStations) {
        if (pathStations.contains(section.getUpStation()) && pathStations.contains(section.getDownStation())) {
            return section.getLine().getSurcharge();
        }
        return BigDecimal.ZERO;
    }


    public BigDecimal charged() {
        return subwayFare;
    }
}
