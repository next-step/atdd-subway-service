package nextstep.subway.station.dto;

import nextstep.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponses {
    private List<StationResponse> list;

    public StationResponses(Line line) {
        this.list = line.getSections().getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getList() {
        return list;
    }
}
