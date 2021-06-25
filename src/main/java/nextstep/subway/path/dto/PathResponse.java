package nextstep.subway.path.dto;

import nextstep.subway.path.util.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private List<SectionEdge> sectionEdges;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(GraphPath graphPath) {
        this.sectionEdges = convertToSectionEdges(graphPath);
        this.stations = convertToStationResponse(graphPath);
        this.distance = calculateTotalDistance();
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    private List<SectionEdge> convertToSectionEdges(GraphPath graphPath) {
        return (List<SectionEdge>) graphPath.getEdgeList()
                .stream()
                .collect(toList());
    }

    private List<StationResponse> convertToStationResponse(GraphPath graphPath) {
        List<Station> stations = graphPath.getVertexList();
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    private int calculateTotalDistance() {
        return sectionEdges.stream()
                .mapToInt(v -> v.getSection().getDistance())
                .sum();
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }
}
