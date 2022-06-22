package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {
    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(LineTest.LINE, new Station("강남역"), new Station("사당역"), 5);
    }

    @Test
    @DisplayName("상행선 같음 확인")
    void equalsUpStation() {
        // then
        assertThat(section.equalsUpStation(new Station("강남역"))).isTrue();
    }

    @Test
    @DisplayName("하행선 같음 확인")
    void equalsDownStation() {
        // then
        assertThat(section.equalsDownStation(new Station("사당역"))).isTrue();
    }

    @Test
    @DisplayName("상행선 / 거리 변경")
    void updateUpStation() {
        // when
        section.updateUpStation(new Station("신림역"), 4);
        // then
        assertThat(section.getUpStation()).isEqualTo(new Station("신림역"));
        assertThat(section.getDistance()).isEqualTo(1);
    }

    @Test
    @DisplayName("하행선 / 거리 변경")
    void updateDownStation() {
        // when
        section.updateDownStation(new Station("신림역"), 4);
        // then
        assertThat(section.getDownStation()).isEqualTo(new Station("신림역"));
        assertThat(section.getDistance()).isEqualTo(1);
    }

    @Test
    @DisplayName("중간 역 삭제 전 새로운 섹션 추가")
    void updateMiddleStation() {
        //given
        final Section nextSection = new Section(LineTest.LINE, new Station("신림역"), new Station("봉천역"), 5);
        // when
        final Section newSection = section.updateMiddleStation(nextSection);
        // then
        assertThat(newSection.getDistance()).isEqualTo(10);
        assertThat(newSection.getUpStation()).isEqualTo(new Station("신림역"));
        assertThat(newSection.getDownStation()).isEqualTo(new Station("사당역"));
    }
}
