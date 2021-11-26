package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    List<FavoritePath> findAllByMemberId(Long memberId);
}
