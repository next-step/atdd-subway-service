package nextstep.subway.path.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PathSections {

    private final List<PathSection> pathSections;

    public PathSections(final List<PathSection> pathSections) {
        this.pathSections = Collections.unmodifiableList(pathSections);
    }

    public List<PathStation> getPathStations() {
        return pathSections.stream()
                .flatMap(pathSection -> pathSection.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
