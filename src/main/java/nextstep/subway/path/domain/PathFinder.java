package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> path;

    PathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static PathFinder of(final List<Long> stationIds, final List<SafeSectionInfo> safeSectionInfos) {
        WeightedMultigraph path = new WeightedMultigraph(DefaultWeightedEdge.class);
        stationIds.forEach(path::addVertex);
        safeSectionInfos.forEach(it -> it.addToPath(path));

        return new PathFinder(path);
    }

    public int sectionSize() {
        return path.edgeSet().size();
    }

    public int stationSize() {
        return path.vertexSet().size();
    }
}
