package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Line 클래스 테스트")
class LineTest {

    Station 양재시민의숲역;
    Station 정자역;

    Line 신분당선;

    @BeforeEach
    void setUp() {
        양재시민의숲역 = new Station("양재시민의숲역");
        정자역 = new Station("정자역");
        신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);
    }

    @DisplayName("Line을 만들고 기본적으로 포함된 역을 확인한다")
    @Test
    void createInstance() {
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 정자역);
    }

    @DisplayName("상행역이 포함된 구간에 새로운 구간을 추가하여 정렬된 역을 확인한다")
    @Test
    void addSectionWithRelocateUpStation() {
        Station 판교역 = new Station("판교역");
        신분당선.addSection(양재시민의숲역, 판교역, 5);

        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 판교역, 정자역);
    }

    @DisplayName("하행역이 포함된 구간에 새로운 구간을 추가하여 정렬된 역을 확인한다")
    @Test
    void addSectionWithRelocateDownStation() {
        Station 광교역 = new Station("광교역");
        신분당선.addSection(정자역, 광교역, 5);

        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 정자역, 광교역);
    }

    @DisplayName("추가하려는 구간이 기존 구간의 길이보다 커 예외가 발생")
    @Test
    void addSectionWithOverflowDistance() {
        Station 판교역 = new Station("판교역");

        assertThatThrownBy(() -> {
            신분당선.addSection(양재시민의숲역, 판교역, 11);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("상하행 어디에도 포함되지 않는 구간을 추가하려 하면 예외가 발생한다")
    @Test
    void addSectionWithNonMatch() {
        Station 광교역 = new Station("광교역");
        Station 광교중악역 = new Station("광교중악역");

        assertThatThrownBy(() -> {
            신분당선.addSection(광교중악역, 광교역, 5);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("등록하려는 구간이 상하행 기존에 포함되어 있다면 예외가 발생한다")
    @Test
    void addSectionWithExist() {
        assertThatThrownBy(() -> {
            신분당선.addSection(정자역, 양재시민의숲역, 5);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("이미 등록된 구간 입니다.");
    }
}
