package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class PathInfoCalculator {

    public static PathInfo calculatePathInfo(GraphPath<Station, StationEdge> path, List<Line> lines) {

        // 거리 계산
        PathDistance pathDistance = sumOfEdgeDistance(path, lines);
        // 거리에 따른 기본요금 계산
        Fare minimumFare = calculateMinimumFare(pathDistance);
        // 노선 추가요금 계산하여 합산
        Fare extraFareAddedFare = minimumFare.add(getHighstExtraFareOfLines(path));
        // 연령에 따른 할인 적용

        return new PathInfo(pathDistance, extraFareAddedFare);
        //return new PathInfo(pathDistance, finalFare);
    }

    public static PathDistance sumOfEdgeDistance(GraphPath<Station, StationEdge> path, List<Line> lines) {
        int weight = (int)Math.floor(path.getWeight());
        return new PathDistance(weight);
    }

    public static Fare calculateMinimumFare(PathDistance pathDistance) {
        return new Fare(pathDistance);
    }

    public static ExtraFare getHighstExtraFareOfLines(GraphPath<Station, StationEdge> path) {
        List<StationEdge> edges = path.getEdgeList();

        List<Line> linesInPath = edges.stream()
                .map(StationEdge::getSection)
                .map(Section::getLine)
                .collect(Collectors.toList());

        ExtraFare maxExtraFare = linesInPath.stream()
                .map(Line::getExtraFare)
                .filter(Objects::nonNull)
                .max(ExtraFare::comparTo)
                .orElse(new ExtraFare());

        return maxExtraFare;
    }
}
