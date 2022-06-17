package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByMember(Member member);

    List<Favorite> findAllByMember(Member member);

    @Modifying
    @Transactional
    @Query("delete from Favorite f where f.member = :member and f.id = :id")
    void deleteByMemberAndId(@Param("member") Member member, @Param("id") Long id);
}
