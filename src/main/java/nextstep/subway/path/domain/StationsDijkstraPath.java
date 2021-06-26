package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.path.PathException;
import nextstep.subway.station.domain.Station;

public class StationsDijkstraPath implements Path {

    private List<Station> stations;
    private List<SectionEdge> sectionEdges;
    private int addMaxFare;
    private LoginMember loginMember;

    public StationsDijkstraPath(List<Station> stations, List<SectionEdge> sectionEdges, LoginMember loginMember) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
        this.addMaxFare = checkAddFareLine(sectionEdges);
        this.loginMember = loginMember;
    }

    @Override
    public int getMinDistance() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getDistance)
            .sum();
    }

    @Override
    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    @Override
    public int getPrice() {
        double discountRate = loginMember.getDiscountRate();
        Fare fare = Fare.of(getMinDistance(), addMaxFare, discountRate);
        return fare.getPrice();
    }

    public static StationsDijkstraPath of(GraphPath<Station, SectionEdge> path, LoginMember loginMember) {
        validatoin(path);
        return new StationsDijkstraPath(path.getVertexList(), path.getEdgeList(), loginMember);
    }

    private static void validatoin(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new PathException(PathException.NOT_CONNECTED);
        }
    }

    private int checkAddFareLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .mapToInt(edge -> edge.getAddFare())
            .max()
            .orElse(0);
    }

}
