package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.exception.ErrorEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineFareTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void 노선_추가요금이_0이하일_경우_예외_발생(int fare) {
        assertThatThrownBy(() -> LineFare.from(fare))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith(ErrorEnum.LINE_FARE_GREATER_ZERO.message());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void 노선_추가요금이_0이상일_경우_객체_생성(int fare) {
        LineFare lineFare = LineFare.from(fare);

        assertThat(lineFare).isNotNull();
    }
}
