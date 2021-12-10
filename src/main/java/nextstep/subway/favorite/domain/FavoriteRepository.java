package nextstep.subway.favorite.domain;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import nextstep.subway.member.domain.*;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findFavoritesByMember(Member member);

    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long loginMemberId);
}
