package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 기능")
public class SectionTest {

    @DisplayName("구간 생성")
    @Test
    public void 구간_생성() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");

        // when
        Section 신분당선_강남구간 = Section.of(신분당선, 강남역, 교대역, Distance.of(10));

        // then
        assertAll(
                () -> assertThat(신분당선_강남구간).isNotNull(),
                () -> assertThat(신분당선_강남구간.hasUpStation(강남역) && 신분당선_강남구간.hasDownStation(교대역))
        );
    }

    @DisplayName("구간 연결가능 검증")
    @Test
    public void 구간_연결가능_검증() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Section 신분당선_강남구간 = Section.of(신분당선, 강남역, 교대역, Distance.of(10));

        // when
        Station 사당역 = new Station("사당역");
        Section 신분당선_사당구간 = Section.of(신분당선, 강남역, 사당역, Distance.of(20));
        boolean 연결가능 = 신분당선_강남구간.connectable(신분당선_사당구간);

        // then
        assertThat(연결가능).isTrue();
    }

    @DisplayName("구간 연결가능 검증_실패")
    @Test
    public void 구간_연결가능_검증_실패() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Section 신분당선_강남구간 = Section.of(신분당선, 강남역, 교대역, Distance.of(10));

        // when
        Section 신분당선_사당구간 = Section.of(신분당선, 강남역, 교대역, Distance.of(20));
        boolean 연결불가능 = 신분당선_강남구간.connectable(신분당선_사당구간);

        // then
        assertThat(연결불가능).isFalse();
    }

}
