package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorites(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = findStationById(favoriteRequest.getSource());
        Station target = findStationById(favoriteRequest.getTarget());
        Favorite saved = favoriteRepository.save(new Favorite(loginMember.getId(), source, target));
        return FavoriteResponse.of(saved);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
