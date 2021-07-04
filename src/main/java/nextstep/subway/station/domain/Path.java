package nextstep.subway.station.domain;

import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1_250;

    private GraphPath graphPath;

    public Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> stations() {
        return graphPath.getVertexList();
    }

    public int distance() {
        return (int)graphPath.getWeight();
    }

    public int fare() {
        // TODO : 라인 추가요금 합산, 거리비례요금 합산, 할인정책 적용
        return BASIC_FARE;
    }
}
