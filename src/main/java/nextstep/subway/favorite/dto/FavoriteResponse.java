package nextstep.subway.favorite.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private List<StationResponse> stationResponseList;

    public static FavoriteResponse of(Long id, List<StationResponse> stationResponses) {
        return new FavoriteResponse(id, stationResponses);
    }

    public FavoriteResponse(Long id, List<StationResponse> stationResponseList) {
        this.id = id;
        this.stationResponseList = stationResponseList;
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStationResponseList() {
        return stationResponseList;
    }
}
