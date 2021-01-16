package nextstep.subway.member.application;

import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class FavoriteService {

    public Long addFavorite(Long id, FavoriteRequest favoriteRequest) {
        return id;
    }

    public FavoriteResponse findFavorites(Long id) {
        return new FavoriteResponse(Arrays.asList(new Favorite(new Station(), new Station())));
    }

    public void deleteFavorite(Long id, Long id1) {
    }
}
