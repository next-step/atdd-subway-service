package nextstep.subway.member.domain;

import nextstep.subway.exception.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    default Member findByIdElseThrow(Long id) {
        return this.findById(id).orElseThrow(NotFoundMemberException::new);
    }
}
