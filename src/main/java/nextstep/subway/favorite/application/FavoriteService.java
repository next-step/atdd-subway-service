package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

	@Transactional
	public FavoriteResponse addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		return null;
	}

	public List<FavoriteResponse> findAllFavorites(Long memberId) {
		return null;
	}

	public void deleteFavorite(Long memberId, String favoriteId) {

	}
}
