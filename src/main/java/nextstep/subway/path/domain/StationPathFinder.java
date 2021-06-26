package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import java.util.Collection;
import java.util.Objects;

public class StationPathFinder implements PathFinder {
    private static final String NO_GRAPH_OR_DIRECTION_EXCEPTION = "조회를 위한 참조 알고리즘이 없거나, 조회 대상이 없습니다.";
    private static final String DISCONNECT_STATION_EXCEPTION = "노선이 연결되어 있지 않습니다.";

    private final ShortestPathAlgorithm shortestPathAlgorithm;
    private final Station source;
    private final Station target;

    public StationPathFinder(ShortestPathAlgorithm shortestPathAlgorithm, Direction direction) {
        validateNullCheck(shortestPathAlgorithm, direction);
        this.shortestPathAlgorithm = shortestPathAlgorithm;
        this.source = direction.getSource();
        this.target = direction.getTarget();
        validateExistPath(source, target);
    }

    private void validateNullCheck(ShortestPathAlgorithm shortestPathAlgorithm, Direction direction) {
        if (Objects.isNull(shortestPathAlgorithm) || Objects.isNull(direction)) {
            throw new IllegalArgumentException(NO_GRAPH_OR_DIRECTION_EXCEPTION);
        }
    }

    private void validateExistPath(Station source, Station target) {
        if (!isExistPath(source, target)) {
            throw new IllegalArgumentException(DISCONNECT_STATION_EXCEPTION);
        }
    }

    private boolean isExistPath(Station source, Station target) {
        return !Objects.isNull(shortestPathAlgorithm.getPath(source, target));
    }

    @Override
    public Collection findPaths() {
        return shortestPathAlgorithm.getPath(source, target).getVertexList();
    }

    @Override
    public int measureDistance() {
        return (int) shortestPathAlgorithm.getPathWeight(source, target);
    }
}
