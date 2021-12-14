package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;

public class FavoriteResponse {
  private Long id;
  private Station source;
  private Station target;

  public FavoriteResponse() {
  }

  public FavoriteResponse(Long id, Station source, Station target) {
    this.id = id;
    this.source = source;
    this.target = target;
  }

  public Long getId() {
    return id;
  }

  public Station getSource() {
    return source;
  }

  public Station getTarget() {
    return target;
  }
}
