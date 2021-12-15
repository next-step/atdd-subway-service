package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    @Transactional
    public FavoriteResponse save(final LoginMember loginMember, final FavoriteRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(final LoginMember loginMember) {
        return null;
    }

    @Transactional
    public void delete(final LoginMember loginMember, final Long id) {
    }
}
