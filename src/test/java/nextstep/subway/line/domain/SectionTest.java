package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SectionTest {
    private Station 강남역;
    private Station 선릉역;
    private Station 역삼역;
    private Section section;
    private Line line;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        역삼역 = new Station("역삼역");
        line = new Line("2호선", "red");
        section = new Section(line, 역삼역, 선릉역, 10);

        line.addSection(section);
    }

    @DisplayName("상행종점 변경 확인")
    @Test
    void 상행_종점_변경() {
        // given
        Section newSection = new Section(line, 강남역, 역삼역, 5);
        // when
        section.updateUpStation(newSection.getDownStation(), newSection.getDistance());
        // then
        assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(newSection.getDownStation()),
                () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("하행종점 변경 확인")
    @Test
    void 하행_종점_변경() {
        // given
        Section newSection = new Section(line, 역삼역, 강남역, 5);
        // when
        section.updateDownStation(newSection.getUpStation(), newSection.getDistance());
        // then
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(newSection.getUpStation()),
                () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

}