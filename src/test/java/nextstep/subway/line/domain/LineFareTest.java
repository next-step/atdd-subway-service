package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.exception.ErrorEnum;
import org.junit.jupiter.api.Test;
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
    @ValueSource(ints = {0, 500, 900})
    void 노선_추가요금이_0이상일_경우_객체_생성(int fare) {
        LineFare lineFare = LineFare.from(fare);

        assertThat(lineFare).isNotNull();
    }

    @Test
    void 추가요금이_같은_경우_객체_동등() {
        LineFare fare1 = LineFare.from(100);
        LineFare fare2 = LineFare.from(100);

        assertThat(fare1).isEqualTo(fare2);
    }

    @Test
    void 추가요금이_같은_경우_객체_동등하지_않음() {
        LineFare fare1 = LineFare.from(100);
        LineFare fare2 = LineFare.from(900);

        assertThat(fare1).isNotEqualTo(fare2);
    }

    @Test
    void LineFare_객체를_추가요금으로_비교() {
        LineFare fare1 = LineFare.from(200);
        LineFare fare2 = LineFare.from(100);
        LineFare fare3 = LineFare.from(100);

        assertAll(
                () -> assertThat(fare1.compareTo(fare2) > 0).isTrue(),
                () -> assertThat(fare2.compareTo(fare1) < 0).isTrue(),
                () -> assertThat(fare2.compareTo(fare3) == 0).isTrue()
        );
    }
}
