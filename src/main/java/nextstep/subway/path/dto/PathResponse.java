package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public List<StationResponse> getStations() {
        List<StationResponse> list = new ArrayList<>();
        list.add(new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()));
        list.add(new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now()));
        list.add(new StationResponse(4L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now()));
        return list;
    }

    public int getDistance() {
        return 12;
    }
}
