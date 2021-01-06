package nextstep.subway.favorites.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMember_Id(long memberId);
}
