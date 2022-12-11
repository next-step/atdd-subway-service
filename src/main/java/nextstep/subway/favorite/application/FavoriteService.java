package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse addFavorite(FavoriteRequest favoriteRequest) {
        /**
         * TODO 예외처리 추가 : source, target 이 같은 경우
         * TODO 예외처리 추가 : 이미 기존에 등록된 항목을 다시 등록할 때
         */
        Station source = findStationById(favoriteRequest.getSourceId());
        Station target = findStationById(favoriteRequest.getTargetId());

        Favorite favorite = favoriteRepository.save(new Favorite(source.getId(), target.getId()));

        return new FavoriteResponse(favorite.getId(), source, target);
    }

    public List<FavoriteResponse> retrieveFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();

        return favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), findStationById(favorite.getSourceId()), findStationById(favorite.getTargetId())))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId) {
        System.out.println("--------------------------------------------");
        System.out.println(favoriteRepository.findById(favoriteId).get().getId());
        favoriteRepository.delete(favoriteRepository.findById(favoriteId).get());
        }

    private Station findStationById(Long stationId) {
        return stationService.findById(stationId);
    }
}
