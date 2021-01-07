package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PathSections {

    private final List<PathSection> pathSections;

    public PathSections(final List<PathSection> pathSections) {
        this.pathSections = Collections.unmodifiableList(pathSections);
    }

    public void forEach(Consumer<PathStation> consumer) {
        getPathStations().forEach(consumer);
    }

    public List<PathStation> getPathStations() {
        return pathSections.stream()
                .flatMap(pathSection -> pathSection.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<PathSection> getPathSections() {
        return pathSections;
    }
}
