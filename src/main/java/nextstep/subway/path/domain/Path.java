package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Path {

    private GraphPath<Station, SectionEdge> pathResult;
    private final List<Station> stations;
    private final int distance;
//    private Fare fare;

    public Path(GraphPath<Station, SectionEdge> pathResult) {
        if (pathResult == null) {
            throw new IllegalArgumentException(Message.ERROR_PATH_NOT_FOUND.showText());
        }
        this.pathResult = pathResult;
        stations = pathResult.getVertexList();
        distance = (int) pathResult.getWeight();
    }

    public Fare getMaxExtraCharge() {
        Comparator<SectionEdge> comparatorByExtraCharge = Comparator.comparingInt(SectionEdge::getExtraCharge);
        return new Fare(pathResult.getEdgeList().stream()
                .max(comparatorByExtraCharge)
                .orElseThrow(NoSuchElementException::new)
                .extraCharge);
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

//    public int getFare() {
//        return fare.getFare();
//    }

}
