package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.constants.StationErrorMessages;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteCreateRequest request) {

        Station sourceStation = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new IllegalArgumentException(StationErrorMessages.STATION_DOES_NOT_EXIST));
        Station targetStation = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new IllegalArgumentException(StationErrorMessages.STATION_DOES_NOT_EXIST));
        Favorite favorite = Favorite.create(loginMember, sourceStation, targetStation);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return FavoriteResponse.getFavoriteResponsesFrom(
                favoriteRepository.findByMemberId(loginMember.getId()));
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.getById(id);
        favoriteRepository.delete(favorite);
    }
}
