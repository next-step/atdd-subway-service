package nextstep.subway.member.domain;

import java.util.Optional;
import nextstep.subway.common.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);
}
