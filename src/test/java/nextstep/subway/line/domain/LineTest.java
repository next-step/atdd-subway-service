package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Line 클래스 테스트")
class LineTest {



    @DisplayName("Line을 만들고 기본적으로 포함된 역을 확인한다")
    @Test
    void createInstance() {
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Station 정자역 = new Station("정자역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);


        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 정자역);
    }

    @DisplayName("상행역이 포함된 구간에 새로운 구간을 추가하여 정렬된 역을 확인한다")
    @Test
    void addSectionWithRelocateUpStation() {
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);

        Station 판교역 = new Station("판교역");
        신분당선.addSection(양재시민의숲역, 판교역, 5);

        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 판교역, 정자역);
    }

    @DisplayName("하행역이 포함된 구간에 새로운 구간을 추가하여 정렬된 역을 확인한다")
    @Test
    void addSectionWithRelocateDownStation() {
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);

        Station 광교역 = new Station("광교역");
        신분당선.addSection(정자역, 광교역, 5);

        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(양재시민의숲역, 정자역, 광교역);
    }

    @DisplayName("상하행 어디에도 포함되지 않는 구간을 추가하려 하면 예외가 발생한다")
    @Test
    void addSectionWithNonMatch() {
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);

        assertThatThrownBy(() -> {
            Station 광교역 = new Station("광교역");
            Station 광교중악역 = new Station("광교중악역");
            신분당선.addSection(광교중악역, 광교역, 5);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("등록하려는 구간이 상하행 기존에 포함되어 있다면 예외가 발생한다")
    @Test
    void addSectionWithExist() {
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재시민의숲역, 정자역, 10);

        assertThatThrownBy(() -> {
            신분당선.addSection(정자역, 양재시민의숲역, 5);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("이미 등록된 구간 입니다.");
    }
}
