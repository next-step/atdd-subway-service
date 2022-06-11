package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.domain.LineTest.지하철역_순서_정렬됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Section 기존_구간;
    private Section 추가_구간;
    private Station 교대역;
    private Station 강남역;
    private Station 삼성역;
    private Station 잠실역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        삼성역 = new Station("삼성역");
        잠실역 = new Station("잠실역");

        이호선 = new Line("이호선", "green");
    }

    @DisplayName("구간 추가 테스트")
    @Test
    void addSection() {
        기존_구간 = new Section(이호선, 교대역, 삼성역, 10);
        추가_구간 = new Section(이호선, 교대역, 강남역, 5);

        Sections sections = new Sections();
        sections.addSection(기존_구간);
        sections.addSection(추가_구간);

        지하철역_순서_정렬됨(sections, Arrays.asList(교대역, 강남역, 삼성역));
    }

    @DisplayName("구간 추가 예외 테스트 - 기존 구간의 길이 보다 더 큰 길이로 등록할 경우")
    @Test
    void addSection_exception_1() {
        기존_구간 = new Section(이호선, 교대역, 삼성역, 10);
        추가_구간 = new Section(이호선, 교대역, 강남역, 11);

        Sections sections = new Sections();
        sections.addSection(기존_구간);

        assertThatThrownBy(() -> sections.addSection(추가_구간)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 추가 예외 테스트 - 기존 구간의 없는 지하철역을 등록할 경우")
    @Test
    void addSection_exception_2() {
        기존_구간 = new Section(이호선, 교대역, 삼성역, 10);
        추가_구간 = new Section(이호선, 잠실역, 강남역, 5);

        Sections sections = new Sections();
        sections.addSection(기존_구간);

        assertThatThrownBy(() -> sections.addSection(추가_구간)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void findSectionByUpStation() {
        기존_구간 = new Section(이호선, 교대역, 강남역, 10);
        추가_구간 = new Section(이호선, 강남역, 삼성역, 5);

        Sections sections = new Sections();
        sections.addSection(기존_구간);
        sections.addSection(추가_구간);

        Section section = sections.findSectionByUpStation(강남역).get();
        assertThat(section.getUpStation().getName()).isEqualTo(강남역.getName());
    }

    @Test
    void findSectionByDownStation() {
        기존_구간 = new Section(이호선, 교대역, 강남역, 10);
        추가_구간 = new Section(이호선, 강남역, 삼성역, 5);

        Sections sections = new Sections();
        sections.addSection(기존_구간);
        sections.addSection(추가_구간);

        Section section = sections.findSectionByDownStation(강남역).get();
        assertThat(section.getDownStation().getName()).isEqualTo(강남역.getName());
    }
}
