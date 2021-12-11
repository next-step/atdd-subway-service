package nextstep.subway.path.dto;

public class PathRequest {
  private Long sourceStationId;
  private Long targetStationId;

  public PathRequest(Long sourceStationId, Long targetStationId) {
    this.sourceStationId = sourceStationId;
    this.targetStationId = targetStationId;
  }

  public Long getSourceStationId() {
    return sourceStationId;
  }

  public Long getTargetStationId() {
    return targetStationId;
  }
}
