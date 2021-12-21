package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByIdMemberId(Long id, Long memberId);
    void deleteByMemberId(Long memberId);
}
