package nextstep.subway.favorite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequest {
    @JsonProperty("source")
    private Long sourceStationId;

    @JsonProperty("target")
    private Long targetStationId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }
}
