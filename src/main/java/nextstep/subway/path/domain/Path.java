package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathEdge;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Line> lines;
    private final Long source;
    private final Long target;
    private List<Station> stations;

    public Path(List<Line> lines, Long source, Long target) {
        this.lines = lines;
        this.source = source;
        this.target = target;
        mapStations(lines);
    }

    public static Path of(List<Line> lines, Long source, Long target) {
        return new Path(lines, source, target);
    }

    public List<PathEdge> toPathEdges() {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .map(this::mapPathEdge)
            .collect(Collectors.toList());
    }

    public List<Station> getStationsBy(List<Long> stationIds) {
        List<Station> result = new ArrayList<>();
        for (Long stationId : stationIds) {
            result.add(getStationBy(stationId));
        }

        return result;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    private void mapStations(List<Line> lines) {
        this.stations = lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
        valid(source, target);
    }

    private void valid(Long source, Long target) {
        if (Objects.equals(source, target)) {
            throw InvalidParameterException.of(ErrorCode.PATH_IN_OUT_SAME);
        }

        if (isContains(source, target)) {
            throw InvalidParameterException.of(ErrorCode.PATH_IN_OUT_NOT_FOUND);
        }
    }

    private boolean isContains(Long source, Long target) {
        return this.stations.stream()
            .filter(station -> isSameStation(source, target, station))
            .count() != 2;
    }

    private boolean isSameStation(Long source, Long target, Station station) {
        return Objects.equals(station.getId(), source) || Objects.equals(station.getId(), target);
    }

    private PathEdge mapPathEdge(Section section) {
        return PathEdge.of(section.getUpStationId(), section.getDownStationId(),
            section.getDistance());
    }

    private Station getStationBy(Long stationId) {
        return this.stations.stream()
            .filter(station -> Objects.equals(station.getId(), stationId))
            .findFirst()
            .orElseThrow(() -> NotFoundException.of(ErrorCode.NOT_EMPTY));
    }
}
