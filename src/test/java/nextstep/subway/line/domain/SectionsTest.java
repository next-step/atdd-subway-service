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

    private Line line = new Line("2호선", "green", 0);
    private Station 강남역 = new Station("강남역");
    private Station 역삼역 = new Station("역삼역");
    private Station 교대역 = new Station("교대역");
    private Station 서초역 = new Station("서초역");
    private Sections sections = new Sections();

    @BeforeEach
    void setUp() {
        sections.add(new Section(line, 강남역, 역삼역, 10));
    }

    @DisplayName("구간을 추가한다. - 구간 외부에 추가")
    @Test
    void add_outside() {
        sections.add(new Section(line, 역삼역, 교대역, 10));

        assertThat(sections.getSections()).hasSize(2);
    }

    @DisplayName("구간을 추가한다. - 구간 내부에 추가")
    @Test
    void add_inside() {
        sections.add(new Section(line, 강남역, 교대역, 7));

        assertThat(sections.getSections()).hasSize(2);
    }

    @DisplayName("구간 추가를 실패한다. - 이미 등록된 구간")
    @Test
    void add_fail_for_already_added() {
        assertThatThrownBy(() -> sections.add(new Section(line, 강남역, 역삼역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간 추가를 실패한다. - 기존 지하철역하고 매치되는 구간이 없음")
    @Test
    void add_fail_for_none_match() {
        assertThatThrownBy(() -> sections.add(new Section(line, 교대역, 서초역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간을 추가를 실패한다. - 구간 내부에 추가시 기존 구간 길이보다 새로운 구간이 길다.")
    @Test
    void add_fail_for_over_distance() {
        assertThatThrownBy(() -> sections.add(new Section(line, 강남역, 서초역, 12)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("지하철 역을 조회한다. - 정렬된 지하철 목록 조회됨")
    @Test
    void getStations() {
        List<Station> stations = sections.getStations();

        assertThat(stations).containsExactly(강남역, 역삼역);
    }

    @DisplayName("지하철 역을 제거한다.")
    @Test
    void removeStation() {
        sections.add(new Section(line, 역삼역, 교대역, 10));

        sections.removeStation(강남역);

        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(역삼역, 교대역);
    }

    @DisplayName("지하철 역을 제거를 실패한다. - 구간이 한개밖에 없을 때 제거 불가")
    @Test
    void removeStation_fail() {
        assertThatThrownBy(() -> sections.removeStation(강남역))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("구간이 1개 이하일 때에는 제거가 불가합니다.");
    }
}
