package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

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

}
