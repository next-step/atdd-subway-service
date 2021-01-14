package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Station 강남역;
    private Station 잠실역;
    private Station 역삼역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        역삼역 = new Station("역삼역");
        이호선 = new Line("이호선", "green", 강남역, 잠실역, 10);
    }

    @DisplayName("구간 추가")
    @Test
    void add() {
        // when
        이호선.addSection(new Section(이호선, 강남역, 역삼역, 7));

        // then
        assertThat(이호선.getSections().getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 역삼역, 잠실역));
    }

    @DisplayName("구간 추가 - 신규 상행역 구간 추가")
    @Test
    void add1() {
        // when
        이호선.addSection(new Section(이호선, 역삼역, 강남역, 7));

        // then
        assertThat(이호선.getSections().getStations()).containsExactlyElementsOf(Arrays.asList(역삼역, 강남역, 잠실역));
    }

    @DisplayName("구간 추가 - 신규 하행역 구간 추가")
    @Test
    void add2() {
        // when
        이호선.addSection(new Section(이호선, 잠실역, 역삼역, 7));

        // then
        assertThat(이호선.getSections().getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 잠실역, 역삼역));
    }

    @DisplayName("구간 추가 예외 - 이미 등록되어 있는 경우")
    @Test
    void addException() {
        assertThatThrownBy(()-> {
            이호선.addSection(new Section(이호선, 강남역, 잠실역, 5));
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 추가 예외 - 포함이 되어있지 않은 경우")
    @Test
    void addException2() {
        assertThatThrownBy(()-> {
            이호선.addSection(new Section(이호선, 역삼역, new Station("왕십리역"), 5));
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 추가 예외 - 기존 구간의 거리보다 긴 경우")
    @Test
    void addException3() {
        assertThatThrownBy(()-> {
            이호선.addSection(new Section(이호선, 강남역, 역삼역, 11));
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 삭제 - 하행 종점역 삭제")
    @Test
    void removeSection() {

        이호선.addSection(new Section(이호선, 강남역, 역삼역, 7));
        이호선.removeSection(잠실역);

        assertThat(이호선.getSections().getStations()).containsExactly(강남역, 역삼역);
    }

    @DisplayName("구간 삭제 - 역 사이 삭제")
    @Test
    void removeSection2() {
        이호선.addSection(new Section(이호선, 강남역, 역삼역, 7));
        이호선.removeSection(역삼역);

        assertThat(이호선.getSections().getStations()).containsExactly(강남역, 잠실역);
    }

    @DisplayName("구간 삭제 - 상행 종점역 삭제")
    @Test
    void removeSection3() {
        // given
        이호선.addSection(new Section(이호선, 강남역, 역삼역, 7));
        // when
        이호선.removeSection(강남역);
        // then
        assertThat(이호선.getSections().getStations()).containsExactly(역삼역, 잠실역);
    }


}