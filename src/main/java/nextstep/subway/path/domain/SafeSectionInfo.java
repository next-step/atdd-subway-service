package nextstep.subway.path.domain;

import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class SafeSectionInfo {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SafeSectionInfo(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void addToPath(WeightedMultigraph path) {
        path.setEdgeWeight(path.addEdge(upStationId, downStationId), distance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SafeSectionInfo that = (SafeSectionInfo) o;
        return distance == that.distance && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId, distance);
    }
}
