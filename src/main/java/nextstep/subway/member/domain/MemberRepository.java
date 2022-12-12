package nextstep.subway.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.common.domain.Email;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(Email email);
}
