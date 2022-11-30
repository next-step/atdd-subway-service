package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    @Transactional
    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(LoginMember member) {
        return Collections.emptyList();
    }

    @Transactional
    public void deleteFavoriteById(Long id, Long memberId) {
    }
}
