package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class PathInfoCalculator {

    public static PathInfo calculatePathInfo(LoginMember loginMember, GraphPath<Station, StationEdge> path, List<Line> lines) {
        PathDistance pathDistance = sumOfEdgeDistance(path); // 경로 거리 계산
        Fare minimumFare = calculateMinimumFare(pathDistance); // 거리에 따른 기본요금 계산
        Fare extraFareAddedFare = minimumFare.add(getHighstExtraFareOfLines(path)); // 노선 추가요금 계산하여 합산
        Fare finalFare = extraFareAddedFare.applyAgeDiscount(loginMember); // 연령에 따른 할인 적용
        return new PathInfo(pathDistance, finalFare);
    }

    protected static PathDistance sumOfEdgeDistance(GraphPath<Station, StationEdge> path) {
        int weight = (int)Math.floor(path.getWeight());
        return new PathDistance(weight);
    }

    protected static Fare calculateMinimumFare(PathDistance pathDistance) {
        return new Fare(pathDistance);
    }

    protected static ExtraFare getHighstExtraFareOfLines(GraphPath<Station, StationEdge> path) {
        List<StationEdge> edges = path.getEdgeList();
        List<Line> linesInPath = getInvolvedLines(edges);
        return getMaxExtraFareOfLines(linesInPath);
    }

    private static List<Line> getInvolvedLines(List<StationEdge> edges) {
        return edges.stream()
                .map(StationEdge::getSection)
                .map(Section::getLine)
                .collect(Collectors.toList());
    }

    private static ExtraFare getMaxExtraFareOfLines(List<Line> linesInPath) {
        return linesInPath.stream()
                .map(Line::getExtraFare)
                .filter(Objects::nonNull)
                .max(ExtraFare::comparTo)
                .orElse(new ExtraFare());
    }
}
