package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Line 도메인 로직 단위 테스트")
class LineTest {

    private Station upStation;
    private Station middleStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행역");
        middleStation = new Station("중간역");
        downStation = new Station("하행역");
    }

    @DisplayName("역 목록 조회 시 상행역에서 하행역 순으로 정렬된 Station 목록 생성")
    @Test
    void getStationsTest() {

        Station[] stations = new Station[10];
        for (int i = 0; i < 10; i++) {
            stations[i] = new Station("정거장 " + (i + 1));
        }

        Line line = new Line("name", "color", stations[0], stations[1], 10);
        for (int i = 1; i < 9; i++) {
            line.addSection(new Section(line, stations[i], stations[i + 1], 10));
        }

        assertThat(line.getStations()).containsExactly(stations);
    }

    @DisplayName("구간 등록 성공 - 기존 구간 가운데에 새 구간 등록")
    @Test
    void addSectionSuccessTest01() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        line.addSection(new Section(line, upStation, middleStation, 50));

        assertThat(line.getStations()).containsExactly(upStation, middleStation, downStation);
    }

    @DisplayName("구간 등록 성공 - 구 상행역에 새로운 상행역을 등록")
    @Test
    void addSectionSuccessTest02() {
        Line line = new Line("name", "color", middleStation, downStation, 100);
        line.addSection(new Section(line, upStation, middleStation, 50));

        assertThat(line.getStations()).containsExactly(upStation, middleStation, downStation);
    }

    @DisplayName("구간 등록 실패 - 기존 구간 사이에 등록하는 경우 기존 구간보다 더 긴 새 구간 등록 불가")
    @Test
    void addSectionFailTest01() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        assertThatExceptionOfType(ModifySectionException.class).isThrownBy(
            () -> line.addSection(new Section(line, upStation, middleStation, 100))
        );
    }

    @DisplayName("구간 등록 실패 - 이미 등록된 구간")
    @Test
    void addSectionFailTest02() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        assertThatExceptionOfType(AddSectionException.class).isThrownBy(
            () -> line.addSection(new Section(line, upStation, downStation, 100))
        );
    }

    @DisplayName("구간 등록 실패 - 기존 구간에 포함되어 있지 않는 상/하행역")
    @Test
    void addSectionFailTest03() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        assertThatExceptionOfType(AddSectionException.class).isThrownBy(
            () -> line.addSection(new Section(line, new Station("다른 상행역"), new Station("다른 하행역"), 100))
        );
    }

    @DisplayName("구간 삭제 성공 - 중간 역 삭제 시 새 구간 생성")
    @Test
    void removeLineSectionSuccessTest01() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        line.addSection(new Section(line, upStation, middleStation, 50));
        line.removeSection(middleStation);

        assertThat(line.getStations()).containsExactly(upStation, downStation);
    }

    @DisplayName("구간 삭제 성공 - 하행 삭제")
    @Test
    void removeLineSectionSuccessTest02() {
        Line line = new Line("name", "color", upStation, downStation, 100);

        Station lastStation = new Station("real real last station");
        Section lastSection = new Section(line, downStation, lastStation, 70);

        line.addSection(new Section(line, upStation, middleStation, 50));
        line.addSection(lastSection);

        line.removeSection(middleStation);
        assertThat(line.getStations()).containsExactly(upStation, downStation, lastStation);
        assertThat(line.getSections()).contains(lastSection);
    }

    @DisplayName("구간 삭제 실패 - 남은 구간이 하나")
    @Test
    void removeLineSectionFailTest() {
        Line line = new Line("name", "color", upStation, downStation, 100);
        assertThatExceptionOfType(DeleteSectionException.class).isThrownBy(
            () -> line.removeSection(middleStation)
        );
    }
}
