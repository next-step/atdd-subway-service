package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간")
class SectionTest {
    private Station 상행역;
    private Station 하행역;

    private Section 구간;

    @BeforeEach
    void setUp() {
        상행역 = Station.from("강남역");
        하행역 = Station.from("역삼역");

        구간 = Section.of(Line.of("2호선", "green"), 상행역, 하행역, 5);
    }

    @Test
    @DisplayName("정적 팩토리 메소드 of를 통해 생성 가능하다.")
    void 생성() {
        assertThat(Section.of(Line.of("2호선", "green"), Station.from("강남역"), Station.from("역삼역"), 5)).isNotNull();
    }

    @Test
    @DisplayName("입력받은 역이 하행역인지 여부를 반환한다.")
    void 하행역_검사() {
        assertAll(() -> assertThat(구간.hasDownStation(상행역)).isFalse(),
                () -> assertThat(구간.hasDownStation(하행역)).isTrue());
    }

    @Test
    @DisplayName("입력받은 구간과 상행역 또는 하행역이 일치하는지 확인할 수 있다.")
    void 상하행역_동일_여부() {
        Station 새_상행역 = Station.from("서초역");
        Station 새_하행역 = new Station("선릉역");

        Section 상행역이_같은_구간 = Section.of(Line.of("2호선", "green"), 상행역, 새_하행역, 5);
        Section 상하행역_다른_구간 = Section.of(Line.of("2호선", "green"), 새_상행역, 새_하행역, 5);

        assertAll(() -> assertThat(구간.hasSameUpOrDownStation(상하행역_다른_구간)).isFalse(),
                () -> assertThat(구간.hasSameUpOrDownStation(상행역이_같은_구간)).isTrue());
    }
}
