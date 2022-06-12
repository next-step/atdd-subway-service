package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.path.vo.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Fare {

    private final GraphPath<Station, SectionEdge> shortestPath;
    private final Member member;

    public Fare(GraphPath<Station, SectionEdge> shortestPath, Member member) {
        this.shortestPath = shortestPath;
        this.member = member;
    }

    public int calcFare() {
        return 0;
    }
}
