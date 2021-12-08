package nextstep.subway.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName : nextstep.subway.favorites.domain
 * fileName : FavoriteRepository
 * author : haedoang
 * date : 2021/12/08
 * description :
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMemberId(Long memberId);
}
