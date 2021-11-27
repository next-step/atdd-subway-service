package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {


    public FavoriteResponse saveFavorite(Member member, FavoriteRequest request) {
        return null;
    }

    public void deleteFavorite(Member member, long maxValue) {
    }
}
