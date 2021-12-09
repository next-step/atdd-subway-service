package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.domain.MemberId;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse save(LoginMember loginMember, FavoriteRequest saveFavorite) {

        Station startStation = stationRepository.findById(saveFavorite.getSource())
                                                .orElseThrow(StationNotFoundException::new);
        Station endStation = stationRepository.findById(saveFavorite.getTarget())
                                                .orElseThrow(StationNotFoundException::new);

        Favorite favorite = Favorite.of(loginMember.getId(), startStation, endStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(savedFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> findFavorites = favoriteRepository.findDistinctByMemberId(new MemberId(loginMember.getId()));
        Favorites favorites = Favorites.of(findFavorites);
        return FavoriteResponse.ofList(favorites.favoriteOfMine());
    }

    public void delete(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                                              .orElseThrow(FavoriteNotFoundException::new);
        favorite.canDeleted(loginMember.toMemberId());
        favoriteRepository.deleteById(favorite.getId());
    }
}
