package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;

public class PathSections {

    private final List<PathSection> pathSections;

    public PathSections(final List<PathSection> pathSections) {
        this.pathSections = Collections.unmodifiableList(pathSections);
    }

    public void forEach(Consumer<PathStation> consumer) {
        getPathStations().forEach(consumer);
    }

    public PathSections findSections(final List<PathStation> shortestPathStations) {
        int size = shortestPathStations.size() - 1;
        return IntStream.range(0, size)
                .mapToObj(index -> findSection(shortestPathStations.get(index), shortestPathStations.get(index + 1)))
                .collect(collectingAndThen(Collectors.toList(), PathSections::new));

    }

    private PathSection findSection(final PathStation source, final PathStation target) {
        PathSection pathSection = pathSections.stream()
                .filter(element -> element.contains(source, target))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        if (pathSection.hasReverseStations(source, target)) {
            return pathSection.reverse();
        }
        return pathSection;
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
