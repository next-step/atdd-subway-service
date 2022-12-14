package nextstep.subway.line.domain;

import nextstep.subway.path.domain.FeeStrategy;
import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }


    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public int getPaidFee(List<Station> stations, int distance, FeeStrategy strategy) {
        List<Section> sections = getRelationSection(lines);
        List<Line> lines = getLineByContainStation(sections, stations);
        int maxExtraFee  = getLineMaxFee(lines);

        return strategy.calculate(distance) + maxExtraFee;
    }


    private Integer getLineMaxFee(List<Line> lines) {
        return lines.stream().max(Comparator.comparing(Line::getExtraFee))
                .map(Line::getExtraFee).orElse(ExtraFee.ofZero().getExtraFee());

    }

    private List<Line> getLineByContainStation(List<Section> sections, List<Station> stations) {
        return sections.stream().filter(section -> isContainUpStationAndDownStation(section, stations))
                .map(Section::getLine).distinct()
                .collect(Collectors.toList());
    }

    private boolean isContainUpStationAndDownStation(Section section, List<Station> station) {
        return station.contains(section.getUpStation()) && station.contains(section.getDownStation());
    }

    private List<Section> getRelationSection(List<Line> lines) {
        return lines
                .stream().flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}
