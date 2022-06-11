package nextstep.subway.favorite.domain;

import java.util.List;
import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(value = "select f from Favorite f "
        + "join fetch Station s "
        + "where f.member = :member")
    List<Favorite> findAllByMember(Member member);
}
