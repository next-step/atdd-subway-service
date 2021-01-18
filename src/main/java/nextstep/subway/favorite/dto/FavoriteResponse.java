package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
public class FavoriteResponse {
	private Long id;

	public FavoriteResponse() {
	}

	public FavoriteResponse(long id) {
		this.id = id;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getId());
	}

	public Long getId() {
		return id;
	}
}
