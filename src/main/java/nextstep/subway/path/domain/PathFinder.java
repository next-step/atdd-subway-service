package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.InvalidPathFinderParameterException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> path;

    PathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static PathFinder of(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        validate(stationIds, safeSectionInfos);

        WeightedMultigraph path = new WeightedMultigraph(DefaultWeightedEdge.class);
        stationIds.forEach(path::addVertex);
        safeSectionInfos.forEach(it -> it.addToPath(path));

        return new PathFinder(path);
    }

    int sectionSize() {
        return path.edgeSet().size();
    }

    int stationSize() {
        return path.vertexSet().size();
    }

    private static void validate(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        if (stationIds == null || safeSectionInfos == null) {
            throw new InvalidPathFinderParameterException("반드시 역, 구간 컬렉션이 존재해야 합니다.");
        }

        if (stationIds.size() == 0 || safeSectionInfos.size() == 0) {
            throw new InvalidPathFinderParameterException("반드시 역, 구간이 하나 이상 존재해야 합니다.");
        }
    }
}
