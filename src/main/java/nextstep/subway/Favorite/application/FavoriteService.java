package nextstep.subway.Favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.Favorite.dto.FavoriteRequest;
import nextstep.subway.Favorite.dto.FavoritesResponse;
import nextstep.subway.auth.domain.LoginMember;

@Service
public class FavoriteService {

	@Transactional
	public FavoritesResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
		return null;
	}

	@Transactional(readOnly = true)
	public List<FavoritesResponse> findAllFavorites(LoginMember loginMember) {
		return null;
	}

	public void deleteFavorite(LoginMember loginMember, long id) {

	}
}
