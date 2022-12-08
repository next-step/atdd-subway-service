package nextstep.subway.favorites.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Long>  {

    Optional<List<Favorites>> findByMember(Member member);

    void deleteByMemberAndId(Member member, Long id);
}
