package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }


}
