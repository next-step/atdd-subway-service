package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Section> sections;
    private Station source;
    private Station target;

    public Path(List<Section> sections, Station source, Station target) {
        validNotSame(source, target);
        validContains(sections, source, target);

        this.sections = sections;
        this.source = source;
        this.target = target;
    }

    public static Path of(List<Line> lines, Station source, Station target) {
        return new Path(toSections(lines), source, target);
    }

    private static List<Section> toSections(List<Line> lines) {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    private void validNotSame(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw InvalidParameterException.of(ErrorCode.PATH_IN_OUT_SAME);
        }
    }

    private void validContains(List<Section> sections, Station source, Station target) {
        boolean sourceMatchCount = sections.stream()
            .anyMatch(section -> section.getStations().contains(source));
        boolean targetMatchCount = sections.stream()
            .anyMatch(section -> section.getStations().contains(target));

        if (!sourceMatchCount || !targetMatchCount) {
            throw InvalidParameterException.of(ErrorCode.PATH_IN_OUT_NOT_FOUND);
        }
    }
}
