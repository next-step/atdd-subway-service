package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class NameTest {

    @DisplayName("이름이 Null 또는 빈 값이면 에러가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void validateNullOrEmptyException(String name) {
        assertThatThrownBy(() -> Name.from(name))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 동일하면 동일한 객체다.")
    @Test
    void equals() {
        Name name1 = Name.from("마곡역");
        Name name2 = Name.from("마곡역");

        assertThat(name1).isEqualTo(name2);
    }
}
