package nextstep.subway.favorite.domain;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(long memberId);

    Optional<Favorite> findByIdAndMemberId(long id, long memberId);

    default Favorites findAllFavorites(long memberId) {
        return Favorites.from(findAllByMemberId(memberId));
    }

}
