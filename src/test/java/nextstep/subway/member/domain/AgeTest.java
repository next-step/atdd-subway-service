package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AgeTest {
    @Test
    @DisplayName("나이 객체 생성")
    void createAge() {
        // when
        Age actual = Age.from(20);

        // then
        Assertions.assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Age.class)
        );
    }

    @Test
    @DisplayName("Null 입력시 나이 생성 실패")
    void createAgeInputNull() {
        // when & then
        assertThatThrownBy(() -> Age.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("나이를 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("0보다 작을 경우 나이 생성 실패")
    void creatAageInputNegative(int age) {
        // when & then
        assertThatThrownBy(() -> Age.from(age))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("나이는 0보다 커야합니다.");
    }
}
