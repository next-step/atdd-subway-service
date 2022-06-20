package nextstep.subway.favorite.domain;

import java.util.List;
import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMember(Member member);
}
