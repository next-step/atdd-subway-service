package nextstep.subway.favorite.domain;

import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FavoriteTest {
    private Station 야탑역 = new Station("야탑역");
    private Station 분당역 = new Station("분당역");
    private Station 모란역 = new Station("모란역");

    @Test
    @DisplayName("라인에 등록된 역이 없을경우 LineHasNotExistStationException이 발생한다")
    void 라인에_등록된_역이_없을경우_LineHasNotExistStationException이_발생한다() {
        Line line = new Line("LINE", "COLOR", 0, 야탑역, 분당역, 10);
        Lines lines = new Lines(Arrays.asList(line));

        assertThatExceptionOfType(LineHasNotExistStationException.class)
                .isThrownBy(() -> Favorite.create(lines, null, 야탑역, 모란역));
    }
    @Test
    @DisplayName("라인에 등록된 역으로 등록시 정상 등록된다")
    void 라인에_등록된_역으로_등록시_정상_등록된다() {
        Line line = new Line("LINE", "COLOR", 0, 야탑역, 분당역, 10);
        Lines lines = new Lines(Arrays.asList(line));

        assertDoesNotThrow(() -> Favorite.create(lines, null, 야탑역, 분당역));
    }
}