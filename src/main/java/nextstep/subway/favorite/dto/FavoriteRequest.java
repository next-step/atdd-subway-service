package nextstep.subway.favorite.dto;

public class FavoriteRequest {
  private final Long sourceStationId;
  private final Long targetStationId;

  public FavoriteRequest(Long source, Long target) {
    this.sourceStationId = source;
    this.targetStationId = target;
  }

  public Long getSourceStationId() {
    return sourceStationId;
  }

  public Long getTargetStationId() {
    return targetStationId;
  }
}
