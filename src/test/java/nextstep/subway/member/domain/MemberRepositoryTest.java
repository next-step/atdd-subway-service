package nextstep.subway.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {

    private Member actual = null;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        actual = Member.of("email@email.com", "password", 20);
    }

    @DisplayName("사용자를 생성한다.")
    @Test
    void save() {
        final Member expected = memberRepository.save(actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("사용자를 조회한다.")
    @Test
    void find() {
        final Member actual1 = memberRepository.save(actual);

        final Member expected = memberRepository.findById(actual1.getId()).get();

        assertThat(actual1).isEqualTo(expected);
    }

    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void update() {
        final Member actual1 = memberRepository.save(actual);
        final Member member = Member.of("update@update.com", "1234", 30);
        actual1.update(member);

        assertAll(
                () -> assertThat(actual1.getEmail()).isEqualTo("update@update.com"),
                () -> assertThat(actual1.getAge()).isEqualTo(30)
        );
    }

    @DisplayName("사용자를 삭제한다.")
    @Test
    void delete() {
        final Member actual1 = memberRepository.save(actual);

        memberRepository.delete(actual1);

        final Member expected = memberRepository.findById(actual1.getId()).orElse(null);

        assertThat(expected).isNull();;
    }
}
