package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
	}

	public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, Long memberId) {
		Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
		Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
		Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, memberId));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long memberId) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
		return FavoriteResponse.of(favorites);
	}

	public void deleteFavorite(Long favoriteId, Long memberId) {
		Favorite favorite = favoriteRepository.findFavoriteByIdAndMemberId(favoriteId, memberId)
				.orElseThrow(() -> new RuntimeException("즐겨찾기가 존재하지 않습니다."));
		favoriteRepository.delete(favorite);
	}
}
