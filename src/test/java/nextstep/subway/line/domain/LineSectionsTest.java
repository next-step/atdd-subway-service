package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineSectionsTest {

    private Station upStation;
    private Station middleStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행역");
        middleStation = new Station("중간역");
        downStation = new Station("하행역");
    }

    @DisplayName("구간 등록 성공 - 기존 구간 가운데에 새 구간 등록")
    @Test
    void addSuccess01() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 100));
        sections.add(new Section(null, upStation, middleStation, 50));

        assertThat(sections.toStations()).containsExactly(upStation, middleStation, downStation);
    }

    @DisplayName("구간 등록 성공 - 구 상행역에 새로운 상행역을 등록")
    @Test
    void addSuccess02() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, middleStation, downStation, 100));
        sections.add(new Section(null, upStation, middleStation, 50));

        assertThat(sections.toStations()).containsExactly(upStation, middleStation, downStation);
    }

    @DisplayName("구간 등록 실패 - 기존 구간 사이에 등록하는 경우 기존 구간보다 더 긴 새 구간 등록 불가")
    @Test
    void addFail01() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 50));

        assertThatExceptionOfType(ModifySectionException.class).isThrownBy(
            () -> sections.add(new Section(null, upStation, middleStation, 100))
        );
    }

    @DisplayName("구간 등록 실패 - 이미 등록된 구간")
    @Test
    void addFail02() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 50));

        assertThatExceptionOfType(AddSectionException.class).isThrownBy(
            () -> sections.add(new Section(null, upStation, downStation, 50))
        );
    }

    @DisplayName("구간 등록 실패 - 기존 구간에 포함되어 있지 않는 상/하행역")
    @Test
    void addFail03() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 50));

        assertThatExceptionOfType(AddSectionException.class).isThrownBy(
            () -> sections.add(new Section(null, new Station("다른 상행역"), new Station("다른 하행역"), 100))
        );
    }

    @DisplayName("구간 삭제 성공 - 중간 역 삭제 시 새 구간 생성")
    @Test
    void deleteSuccess() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 100));
        sections.add(new Section(null, upStation, middleStation, 50));

        sections.delete(null, middleStation);
        assertThat(sections.toStations()).containsExactly(upStation, downStation);
    }

    @DisplayName("구간 삭제 성공 - 하행 삭제")
    @Test
    void removeLineSectionSuccessTest02() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 100));
        sections.add(new Section(null, upStation, middleStation, 50));

        sections.delete(null, upStation);
        assertThat(sections.toStations()).containsExactly(middleStation, downStation);
    }

    @DisplayName("구간 삭제 실패 - 남은 구간이 하나")
    @Test
    void removeLineSectionFailTest() {
        LineSections sections = new LineSections();
        sections.add(new Section(null, upStation, downStation, 100));

        assertThatExceptionOfType(DeleteSectionException.class).isThrownBy(
            () -> sections.delete(null, upStation)
        );
    }
}
