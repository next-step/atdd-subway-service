package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간")
class SectionTest {
    @Test
    @DisplayName("정적 팩토리 메소드 of를 통해 생성 가능하다.")
    void 생성() {
        assertThat(Section.of(Line.of("2호선", "green"), new Station("강남역"), new Station("역삼역"), 5)).isNotNull();
    }

    @Test
    @DisplayName("입력받은 역이 하행역인지 여부를 반환한다.")
    void 하행역_검사() {
        Station 상행역 = new Station("강남역");
        Station 하행역 = new Station("역삼역");

        Section section = Section.of(Line.of("2호선", "green"), 상행역, 하행역, 5);
        assertAll(() -> assertThat(section.hasDownStation(상행역)).isFalse(),
                () -> assertThat(section.hasDownStation(하행역)).isTrue());
    }
}
