package nextstep.subway.favorite.dto;

import java.util.Objects;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;


	public FavoriteResponse() {
	}

	public FavoriteResponse(Long id, Station source, Station target) {
		this.id = id;
		this.source = StationResponse.of(source);
		this.target = StationResponse.of(target);
	}

	public FavoriteResponse(Favorite favorite) {
		this.id = favorite.getId();
		this.source = StationResponse.of(favorite.getSource());
		this.target = StationResponse.of(favorite.getTarget());
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
	}

	public Long getId() {
		return id;
	}

	public StationResponse getSource() {
		return source;
	}

	public StationResponse getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FavoriteResponse that = (FavoriteResponse)o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getSource(), that.getSource())
			&& Objects.equals(getTarget(), that.getTarget());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getSource(), getTarget());
	}
}
