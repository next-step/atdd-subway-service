package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;

public class PathStations {

    private final List<PathStation> pathStations;

    public PathStations(final List<PathStation> pathStations) {
        this.pathStations = Collections.unmodifiableList(pathStations);
    }

    public PathSections toPathSections(final List<PathSection> allSections) {
        int size = pathStations.size() - 1;
        return IntStream.range(0, size)
                .mapToObj(index -> findSection(index, allSections))
                .collect(collectingAndThen(Collectors.toList(), PathSections::new));
    }

    private PathSection findSection(final int index, final List<PathSection> allSections) {
        PathStation source = pathStations.get(index);
        PathStation target = pathStations.get(index + 1);

        PathSection pathSection = allSections.stream()
                .filter(element -> element.contains(source, target))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        if (pathSection.hasReverseStations(source, target)) {
            return pathSection.reverse();
        }
        return pathSection;
    }
}
