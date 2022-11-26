package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MemberRepository 테스트")
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 사용자를 조회")
    void findByEmail() {
        memberRepository.save(new Member("testuser@test.com", "password157#", 20));

        assertTrue(memberRepository.findByEmail("testuser@test.com").isPresent());
    }
}
