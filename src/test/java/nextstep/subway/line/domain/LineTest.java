package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선 기능 테스트")
class LineTest {
    private Station 강남역;
    private Station 정자역;
    private Station 광교역;
    private Line 신분당선;

    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "green");
    }

    @Test
    @DisplayName("노선에 등록된 역 조회")
    void findStations() {
        Line 신분당선 = new Line("신분당선", "green", 강남역, 광교역, 30);

        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("등록된 구간 중간에 상행 기준으로 역을 추가할 경우")
    @Test
    void addSectionInMiddleAsUpStation() {
        신분당선.addSection(강남역, 광교역, 10);
        신분당선.addSection(강남역, 정자역, 5);

        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 정자역, 광교역);
    }

    @DisplayName("등록된 구간 중간에 하행 기준으로 역을 추가할 경우")
    @Test
    void addSectionInMiddleAsDownStation() {
        신분당선.addSection(강남역, 광교역, 10);
        신분당선.addSection(정자역, 광교역, 5);

        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 정자역, 광교역);
    }

    @DisplayName("상행역 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        신분당선.addSection(강남역, 광교역, 10);
        신분당선.addSection(정자역, 강남역, 5);

        assertThat(신분당선.getStations()).containsExactly(정자역, 강남역, 광교역);
    }

    @DisplayName("하행역 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        신분당선.addSection(강남역, 정자역, 10);
        신분당선.addSection(정자역, 광교역, 5);

        assertThat(신분당선.getSections().size()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 정자역, 광교역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        신분당선.addSection(강남역, 광교역, 10);

        assertThatThrownBy(() -> 신분당선.addSection(강남역, 광교역, 5))
            .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("등록할 수 없는 구간 추가시 발생")
    @Test
    void addInValidRangeSection() {
        Station 사당역 = new Station("사당역");
        Station 서울대입구역 = new Station("서울대입구역");
        신분당선.addSection(강남역, 광교역, 10);

        assertThatThrownBy(() -> 신분당선.addSection(사당역, 서울대입구역, 5))
            .isInstanceOf(RuntimeException.class);
    }
}
