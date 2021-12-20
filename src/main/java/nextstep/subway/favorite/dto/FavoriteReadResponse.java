package nextstep.subway.favorite.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public class FavoriteReadResponse {
	private List<FavoriteResponse> favoriteResponses = new ArrayList<>();

	public FavoriteReadResponse() {
	}

	public FavoriteReadResponse(List<FavoriteResponse> favoriteResponses) {
		this.favoriteResponses = favoriteResponses;
	}

	@JsonValue
	public List<FavoriteResponse> getFavoriteResponses() {
		return favoriteResponses;
	}
}
