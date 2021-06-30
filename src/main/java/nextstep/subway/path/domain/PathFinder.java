package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.infra.JgraphShortestPath;
import nextstep.subway.station.domain.Station;
import java.util.Objects;

public class PathFinder {
    private final ShortestPath shortestPath;

    public PathFinder(final Lines lines) {
        this(new JgraphShortestPath(lines));
    }

    public PathFinder(final ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Path getPath(final Station source, final Station target) {
        validation(source, target);

        return shortestPath.getPath(source, target);
    }

    private void validation(final Station source, final Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

}
