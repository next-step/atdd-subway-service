package nextstep.subway.map.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.BadRequestApiException;
import nextstep.subway.fare.domain.DiscountPolicy;
import nextstep.subway.fare.domain.SubwayFare;
import nextstep.subway.fare.domain.SubwayFareCalculator;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.apache.commons.lang3.ObjectUtils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayMap {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final Sections sections;

    private SubwayMap(List<Section> sections) {
        this.sections = Sections.of(sections);

        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public static SubwayMap of(List<Section> sections) {
        return new SubwayMap(sections);
    }

    public PathResponse findShortestPath(Station source, Station target, LoginMember loginMember) {
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (ObjectUtils.isEmpty(path)) {
            throw new BadRequestApiException(ErrorCode.UNCOUPLED_PATH);
        }

        Lines lines = sections.getLines(path.getVertexList());
        Stations stations = Stations.of(path.getVertexList());
        int distance = (int) path.getWeight();
        int fare = SubwayFareCalculator.calculate(distance) + lines.getHighestExtraFare();

        DiscountPolicy discountPolicy = DiscountPolicy.of(loginMember.getAge());
        SubwayFare subwayFare = SubwayFare.of(fare, discountPolicy);
        return PathResponse.of(stations.toResponse(), distance, subwayFare.calculateDiscountFare());
    }
}
