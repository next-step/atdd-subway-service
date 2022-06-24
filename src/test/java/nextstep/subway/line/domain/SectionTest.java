package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간")
class SectionTest {
    private static final int 거리 = 5;

    private Station 강남역;
    private Station 역삼역;

    private Line 이호선;
    private Section 강남_역삼_구간;

    @BeforeEach
    void setUp() {
        강남역 = Station.from("강남역");
        역삼역 = Station.from("역삼역");

        이호선 = Line.of("2호선", "green");
        강남_역삼_구간 = Section.of(이호선, 강남역, 역삼역, 거리);
    }

    @Test
    @DisplayName("정적 팩토리 메소드 of를 통해 생성 가능하다.")
    void 생성() {
        assertThat(Section.of(이호선, 강남역, 역삼역, 거리)).isNotNull();
    }

    @Test
    @DisplayName("입력받은 역이 하행역인지 여부를 반환한다.")
    void 하행역_검사() {
        assertAll(() -> assertThat(강남_역삼_구간.hasDownStation(강남역)).isFalse(),
                () -> assertThat(강남_역삼_구간.hasDownStation(역삼역)).isTrue());
    }

    @Test
    @DisplayName("입력받은 구간과 상행역 또는 하행역이 일치하는지 확인할 수 있다.")
    void 상하행역_동일_여부() {
        Station 새_상행역 = Station.from("서초역");
        Station 새_하행역 = new Station("선릉역");

        Section 상행역이_같은_구간 = Section.of(이호선, 강남역, 새_하행역, 거리);
        Section 상하행역_다른_구간 = Section.of(이호선, 새_상행역, 새_하행역, 거리);

        assertAll(() -> assertThat(강남_역삼_구간.hasSameUpOrDownStation(상하행역_다른_구간)).isFalse(),
                () -> assertThat(강남_역삼_구간.hasSameUpOrDownStation(상행역이_같은_구간)).isTrue());
    }

    @Test
    @DisplayName("연결된 두 구간을 하나의 구간으로 병합할 수 있다.")
    void 연결된_두_구간_병합() {
        Station 선릉역 = Station.from("선릉역");

        Section 강남_역삼_구간 = Section.of(이호선, 강남역, 역삼역, 거리);
        Section 역삼_선릉_구간 = Section.of(이호선, 역삼역, 선릉역, 거리);

        assertThat(강남_역삼_구간.merge(역삼_선릉_구간)).isEqualTo(Section.of(이호선, 강남역, 선릉역, 거리 + 거리));
    }
}
