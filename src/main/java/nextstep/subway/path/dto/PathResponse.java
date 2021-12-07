package nextstep.subway.path.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.path.dto
 * fileName : PathResponse
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@NoArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;

    @JsonIgnore
    private Distance distance;

    private PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, Distance distance) {
        List<StationResponse> stationResponses =
                stations.stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }


}
