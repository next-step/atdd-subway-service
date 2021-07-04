package nextstep.subway.favorite.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoriteRepository {
    public List<Favorite> findByMemberId(Long id) {
        return null;
    }
}
