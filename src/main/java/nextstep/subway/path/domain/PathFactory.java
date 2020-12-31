package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathCreationException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFactory {
    public static WeightedMultigraph of(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        validate(stationIds, safeSectionInfos);

        WeightedMultigraph path = new WeightedMultigraph(DefaultWeightedEdge.class);
        stationIds.forEach(path::addVertex);
        safeSectionInfos.forEach(it -> it.addToPath(path));

        return path;
    }

    private static void validate(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        if (stationIds == null || safeSectionInfos == null) {
            throw new PathCreationException("반드시 역, 구간 컬렉션이 존재해야 합니다.");
        }

        if (stationIds.size() == 0 || safeSectionInfos.size() == 0) {
            throw new PathCreationException("반드시 역, 구간이 하나 이상 존재해야 합니다.");
        }
    }
}
