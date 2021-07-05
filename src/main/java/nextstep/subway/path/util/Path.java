package nextstep.subway.path.util;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Path {

    private List<StationResponse> stations;
    private List<SectionEdge> sectionEdges;
    private int distance;
    private int extraFare;

    public Path() {
    }

    public Path(GraphPath graphPath) {
        this.sectionEdges = convertToSectionEdges(graphPath);
        this.stations = convertToStationResponse(graphPath);
        this.distance = calculateTotalDistance();
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

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public List<SectionEdge> getSectionEdges() {
        return Collections.unmodifiableList(sectionEdges);
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxExtraFare() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }
}
