package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.DistanceTest.거리_생성;
import static nextstep.subway.station.domain.StationTest.지하철_생성;
import static org.assertj.core.api.Assertions.*;

class SectionTest {

    @DisplayName("구간의 상/하행의 동일 여부 확인은 정상 동작해야 한다")
    @Test
    void upOrDownStationCheckTest() {
        // given
        Station 상행역 = 지하철_생성("상행역");
        Station 하행역 = 지하철_생성("하행역");
        Section 구간 = 구간_생성(상행역, 하행역, 거리_생성(10));

        // when
        boolean 상행_체크_상행 = 구간.isSameUpStation(상행역);
        boolean 상행_체크_하행 = 구간.isSameUpStation(하행역);
        boolean 하행_체크_상행 = 구간.isSameDownStation(상행역);
        boolean 하행_체크_하행 = 구간.isSameDownStation(하행역);

        // then
        assertThat(상행_체크_상행).isTrue();
        assertThat(상행_체크_하행).isFalse();
        assertThat(하행_체크_상행).isFalse();
        assertThat(하행_체크_하행).isTrue();
    }

    @DisplayName("구간의 상/하행역 변경시 정상 적용되어야 한다")
    @Test
    void upOrDownStationUpdateTest() {
        // given
        Station 상행역 = 지하철_생성("상행역");
        Station 하행역 = 지하철_생성("하행역");
        Station 변경될_상행역 = 지하철_생성("변경될_상행역");
        Station 변경될_하행역 = 지하철_생성("변경될_하행역");
        Section 상행역이_변경될_구간 = 구간_생성(상행역, 하행역, 거리_생성(10));
        Section 하행역이_변경될_구간 = 구간_생성(상행역, 하행역, 거리_생성(10));

        // when
        상행역이_변경될_구간.updateUpStation(변경될_상행역, 거리_생성(3));
        하행역이_변경될_구간.updateDownStation(변경될_하행역, 거리_생성(4));

        // then
        assertThat(상행역이_변경될_구간.isSameUpStation(변경될_상행역)).isTrue();
        assertThat(상행역이_변경될_구간.isSameDownStation(하행역)).isTrue();
        assertThat(상행역이_변경될_구간.getDistance().getValue()).isEqualTo(7);
        assertThat(하행역이_변경될_구간.isSameUpStation(상행역)).isTrue();
        assertThat(하행역이_변경될_구간.isSameDownStation(변경될_하행역)).isTrue();
        assertThat(하행역이_변경될_구간.getDistance().getValue()).isEqualTo(6);
    }

    @DisplayName("구간의 상/하행역에 기존보다 크거나 같은 거리로 변경 요청을 하면 예외가 발생해야 한다")
    @Test
    void upOrDownStationUpdateByBiggestDistanceTest() {
        // given
        Station 상행역 = 지하철_생성("상행역");
        Station 하행역 = 지하철_생성("하행역");
        Station 변경될_역 = 지하철_생성("상행역");
        Section 구간 = 구간_생성(상행역, 하행역, new Distance(10));

        // then
        assertThatThrownBy(() -> 구간.updateUpStation(변경될_역, new Distance(11))).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> 구간.updateDownStation(변경될_역, new Distance(11))).isInstanceOf(RuntimeException.class);
    }

    public static Section 구간_생성(Station upStation, Station downStation, Distance distance) {
        return new Section(new Line(), upStation, downStation, distance);
    }
}
