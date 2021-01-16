package nextstep.subway.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m"
            + " left join fetch m.favorites fav"
            + " left join fetch fav.departureStation source"
            + " left join fetch fav.arrivalStation target"
            + " where m.id = :id")
    Member findByMemberId(@Param("id") Long id);
}
