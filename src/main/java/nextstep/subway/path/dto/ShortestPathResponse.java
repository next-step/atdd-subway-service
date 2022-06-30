package nextstep.subway.path.dto;

import java.util.List;
import javax.xml.xpath.XPathException;
import nextstep.subway.path.domain.SectionWeightedEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

public class ShortestPathResponse {
    private int distance;
    private List<StationResponse> stations;

    public ShortestPathResponse() {

    }

    private ShortestPathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static ShortestPathResponse of(GraphPath<Station, SectionWeightedEdge> path, List<StationResponse> stationResponses) {
        return new ShortestPathResponse((int) path.getWeight(), stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
