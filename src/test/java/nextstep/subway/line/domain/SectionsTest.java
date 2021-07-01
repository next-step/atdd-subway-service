package nextstep.subway.line.domain;

import nextstep.subway.line.exception.IllegalSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Line line2 = new Line("2호선", "green", 0);
    private Line line3 = new Line("3호선", "orange", 900);
    private Station 강남역 = new Station("강남역");
    private Station 역삼역 = new Station("역삼역");
    private Station 교대역 = new Station("교대역");
    private Station 서초역 = new Station("서초역");
    private Station 도곡역 = new Station("도곡역");
    private Station 양재역 = new Station("양재역");
    private Sections sections1 = new Sections();
    private Sections sections2 = new Sections();

    @BeforeEach
    void setUp() {
        sections1.add(new Section(line2, 강남역, 역삼역, 10));
        sections2.add(new Section(line2, 강남역, 양재역, 20));
        sections2.add(new Section(line3, 교대역, 양재역, 20));
    }

    @DisplayName("구간을 추가한다. - 구간 외부에 추가")
    @Test
    void add_outside() {
        sections1.add(new Section(line2, 교대역, 강남역, 10));

        assertThat(sections1.getSections()).hasSize(2);
    }

    @DisplayName("구간을 추가한다. - 구간 내부에 추가")
    @Test
    void add_inside() {
        sections1.add(new Section(line2, 강남역, 교대역, 7));

        assertThat(sections1.getSections()).hasSize(2);
    }

    @DisplayName("구간 추가를 실패한다. - 이미 등록된 구간")
    @Test
    void add_fail_for_already_added() {
        assertThatThrownBy(() -> sections1.add(new Section(line2, 강남역, 역삼역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간 추가를 실패한다. - 기존 지하철역하고 매치되는 구간이 없음")
    @Test
    void add_fail_for_none_match() {
        assertThatThrownBy(() -> sections1.add(new Section(line2, 서초역, 교대역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간을 추가를 실패한다. - 구간 내부에 추가시 기존 구간 길이보다 새로운 구간이 길다.")
    @Test
    void add_fail_for_over_distance() {
        assertThatThrownBy(() -> sections1.add(new Section(line2, 강남역, 서초역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("지하철 역을 조회한다. - 정렬된 지하철 목록 조회됨")
    @Test
    void getStations() {
        List<Station> stations = sections1.getStations();

        assertThat(stations).containsExactly(강남역, 역삼역);
    }

    @DisplayName("지하철 역을 제거한다.")
    @Test
    void removeStation() {
        sections1.add(new Section(line2, 역삼역, 교대역, 10));

        sections1.removeStation(강남역);

        List<Station> stations = sections1.getStations();
        assertThat(stations).containsExactly(역삼역, 교대역);
    }

    @DisplayName("지하철 역을 제거를 실패한다. - 구간이 한개밖에 없을 때 제거 불가")
    @Test
    void removeStation_fail() {
        assertThatThrownBy(() -> sections1.removeStation(강남역))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("구간이 1개 이하일 때에는 제거가 불가합니다.");
    }

    @DisplayName("노선별 가지고 있는 노선 금액 중 큰 값을 찾는다.")
    @Test
    void findMaxLineFare() {
        int expectedFare = 900;

        int maxLineFare = sections2.findMaxLineFare();

        assertThat(maxLineFare).isEqualTo(expectedFare);
    }
}
