package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.Objects;

public class FavoriteResponse {
	private Long id;
	private FavoriteStationResponse source;
	private FavoriteStationResponse target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(Favorite favorite) {
		this.id = favorite.getId();
		this.source = FavoriteStationResponse.of(favorite.getSource());
		this.target = FavoriteStationResponse.of(favorite.getTarget());
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FavoriteStationResponse getSource() {
		return source;
	}

	public void setSource(FavoriteStationResponse source) {
		this.source = source;
	}

	public FavoriteStationResponse getTarget() {
		return target;
	}

	public void setTarget(FavoriteStationResponse target) {
		this.target = target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FavoriteResponse that = (FavoriteResponse) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(source, that.source) &&
				Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, source, target);
	}
}
