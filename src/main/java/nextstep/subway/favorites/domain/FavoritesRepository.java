package nextstep.subway.favorites.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findAllByMember_Id(long memberId);
}
