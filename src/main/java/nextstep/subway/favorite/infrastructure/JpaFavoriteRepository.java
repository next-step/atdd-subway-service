package nextstep.subway.favorite.infrastructure;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {
    @Override
    @Query("select f from Favorite f " +
            "join fetch f.member " +
            "join fetch f.source " +
            "join fetch f.target " +
            "where f.member = :member")
    List<Favorite> findAllByMember(@Param("member") Member member);
}
