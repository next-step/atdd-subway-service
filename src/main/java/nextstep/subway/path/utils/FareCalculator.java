package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FareCalculator {

    public static final int BASE_FARE = 1_250;
    public static final int THRESHOLD_SECOND = 50;
    public static final int THRESHOLD_FIRST = 10;
    public static final int CHILD_THRESHOLD = 6;
    public static final int YOUTH_THRESHOLD = 13;
    public static final int ADULT_THRESHOLD = 19;
    public static final int DISCOUNT_BASE = 350;
    public static final double CHILD_DISCOUNT_PERCENTAGE = 0.5;
    public static final double YOUTH_DISCOUNT_PERCENTAGE = 0.8;

    public BigDecimal calculate(List<Line> lines, Path path, Integer age) {
        int distance = path.getDistance();
        BigDecimal fare = new BigDecimal(BASE_FARE + calculateOverFare(distance)).add(addSurcharge(lines, path));
        return ageDiscount(fare, age);
    }

    private BigDecimal ageDiscount(BigDecimal fare, Integer age) {
        if (age == null) {
            return fare;
        }
        if (age >= CHILD_THRESHOLD && age < YOUTH_THRESHOLD) {
            return fare.subtract(BigDecimal.valueOf(DISCOUNT_BASE))
                    .multiply(BigDecimal.valueOf(CHILD_DISCOUNT_PERCENTAGE));
        }
        if (age >= YOUTH_THRESHOLD && age < ADULT_THRESHOLD) {
            return fare.subtract(BigDecimal.valueOf(DISCOUNT_BASE))
                    .multiply(BigDecimal.valueOf(YOUTH_DISCOUNT_PERCENTAGE));
        }
        return fare;
    }

    private BigDecimal addSurcharge(List<Line> lines, Path path) {
        List<Section> sections = getSections(lines);
        List<StationResponse> stationResponses = path.getStationResponses();
        List<Line> useLines = getUseLines(sections, stationResponses);
        return getMaxSurcharge(useLines);
    }

    private List<Section> getSections(List<Line> lines) {
        List<Section> sections = lines.stream()
                .flatMap(line -> line.getSections()
                        .getSections()
                        .stream())
                .collect(Collectors.toList());
        return sections;
    }

    private BigDecimal getMaxSurcharge(List<Line> useLines) {
        BigDecimal max = useLines.stream()
                .map(line -> line.getSurcharge())
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("추가요금의 최대값이 없습니다"));
        return max;
    }

    private List<Line> getUseLines(List<Section> sections, List<StationResponse> stationResponses) {
        List<Line> useLines = new ArrayList<>();
        List<Long> stationIds = new ArrayList<>();
        makeSectionStationIdForm(stationResponses, stationIds);
        sections.stream()
                .filter(section -> isSection(section, stationIds))
                .forEach(section -> useLines.add(section.getLine()));
        return useLines;
    }

    private void makeSectionStationIdForm(List<StationResponse> stationResponses, List<Long> stationIds) {
        for (int i = 0; i < stationResponses.size() - 1; i++) {
            stationIds.add(stationResponses.get(0)
                    .getId());
            stationIds.add(stationResponses.get(1)
                    .getId());
        }
    }

    private boolean isSection(Section section, List<Long> stationsIds) {
        return section.getStationIds()
                .containsAll(stationsIds);
    }

    private int calculateOverFare(int distance) {
        if (distance <= THRESHOLD_SECOND) {
            return calculateOverFareLessOrEqualFiftyKilometers(distance - THRESHOLD_FIRST);
        }
        return calculateOverFare(THRESHOLD_SECOND) + calculateOverFareOverFiftyKilometers(distance - THRESHOLD_SECOND);
    }

    private int calculateOverFareOverFiftyKilometers(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 8) + 1) * 100);
    }

    private int calculateOverFareLessOrEqualFiftyKilometers(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }
}
