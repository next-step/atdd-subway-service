package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
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

  public static FavoriteResponse of(Favorite favorite) {
    return new FavoriteResponse(favorite.getId(), favorite.getSourceStation(), favorite.getTargetStation());
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
