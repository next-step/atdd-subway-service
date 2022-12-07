package nextstep.subway.favorites.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long>  {

    List<Favorites> findByMember(Member member);

    void deleteByMemberAndId(Member member, Long id);
}
