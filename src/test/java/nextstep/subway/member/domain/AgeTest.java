package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AgeTest {
    @Test
    @DisplayName("나이 생성")
    void createAge() {
        // when
        final Age age = Age.of(13);
        // then
        assertThat(age.getValue()).isEqualTo(13);
    }

    @Test
    @DisplayName("나이 음수 오류")
    void minAgeException() {
        // then
        assertThatThrownBy(() -> Age.of(-1))
                .isInstanceOf(MemberException.class);
    }
}
