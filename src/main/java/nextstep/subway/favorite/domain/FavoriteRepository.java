package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);

    List<Favorite> findAllByMember(Member member);

    Optional<Favorite> findById(Long favoriteId);

    void deleteById(Long favoriteId);
}
