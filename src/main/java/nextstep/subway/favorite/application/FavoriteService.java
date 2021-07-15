package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {

	private FavoriteRepository favoriteRepository;

	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}

	public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
		return null;
	}

	public List<FavoriteResponse> findAllFavorites() {
		return null;
	}

	public void deleteFavoriteById(Long id) {

	}
}
