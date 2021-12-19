package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> path;
    private final int distance;
    private final Sections pathSections;

    public Path(List<Station> path, double distance) {
        validate(path, (int) distance);
        this.path = path;
        this.distance = (int) distance;
        this.pathSections = new Sections();
    }

    public Path(List<Station> path, int distance, Sections pathSections) {
        validate(path, distance);
        this.path = path;
        this.distance = distance;
        this.pathSections = pathSections;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public List<Line> getLineList() {
        return pathSections.getSections().stream()
            .map(Section::getLine)
            .collect(Collectors.toList());
    }

    public void validate(List<Station> path, int distance) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("path is empty");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Not valid pathWeight. " + distance);
        }
    }

}
