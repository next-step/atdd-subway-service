package nextstep.subway.favorite.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;

@Service
@Transactional
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
	}

	public FavoriteResponse save(FavoriteRequest favoriteRequest) {
		Favorite favorite = favoriteRepository.save(new Favorite(stationService.findStationById(favoriteRequest.getSourceId()), stationService.findStationById(favoriteRequest.getTargetId())));
		return FavoriteResponse.of(favorite);
	}
}
