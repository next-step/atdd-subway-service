package nextstep.subway.line.domain;

import nextstep.subway.line.exception.AlreadyAddSectionException;
import nextstep.subway.line.exception.ExistsOnlyOneSectionInLineException;
import nextstep.subway.line.exception.NotExistStationInLineException;
import nextstep.subway.line.exception.OutOfDistanceRangeException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Sections sections;
    private Line 이호선;
    private Station 사당역;
    private Station 삼성역;
    private Station 강남역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("이호선", "bg-blue");
        사당역 = new Station("사당역");
        강남역 = new Station("강남역");
        sections = new Sections(new Section(이호선, 사당역, 강남역, 10));
        삼성역 = new Station("삼성역");
        sections.addSection(new Section(이호선, 강남역, 삼성역, 3));
    }

    @DisplayName("지하철 역 조회")
    @Test
    void getStationsNames() {
        List<String> stationNames = getStationNames();

        assertThat(stationNames).containsExactly("사당역", "강남역", "삼성역");
    }

    @DisplayName("지하철 구간에 종점역 추가")
    @Test
    void addStationOuterSection() {
        Station 잠실역 = new Station("잠실역");
        Section newSection = new Section(이호선, 삼성역, 잠실역, 3);

        sections.addSection(newSection);

        assertThat(getStationNames()).containsExactly("사당역", "강남역", "삼성역", "잠실역");
        assertThat(sections.getTotalDistance()).isEqualTo(new Distance(16));
    }

    @DisplayName("지하철 구간 내부에 지하철역 추가")
    @Test
    void addStationInnerSection() {
        Station 역삼역 = new Station("역삼역");
        Section newSection = new Section(이호선, 강남역, 역삼역, 1);

        sections.addSection(newSection);

        assertThat(getStationNames()).containsExactly("사당역", "강남역", "역삼역", "삼성역");
        assertThat(sections.getTotalDistance()).isEqualTo(new Distance(13));
    }

    @DisplayName("이미 등록된 구간은 등록할 수 없다.")
    @Test
    void alreadyAddSection() {
        Section addSection = new Section(이호선, 사당역, 강남역, 10);

        ThrowableAssert.ThrowingCallable throwingCallable = () -> sections.addSection(addSection);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlreadyAddSectionException.class);
    }

    @DisplayName("등록하려는 구간의 역이 노선에 존재하지 않으면 등록할 수 없다")
    @Test
    void notExistStationInLineException() {
        Station 홍대입구역 = new Station("홍대입구역");
        Station 신촌역 = new Station("신촌역");
        Section addSection = new Section(이호선, 홍대입구역, 신촌역, 10);

        ThrowableAssert.ThrowingCallable throwingCallable = () -> sections.addSection(addSection);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(NotExistStationInLineException.class);
    }

    @DisplayName("지하철 구간 중간역 삭제")
    @Test
    void deleteSection() {
        sections.removeSection(이호선, 강남역);

        assertThat(getStationNames()).containsExactly("사당역", "삼성역");
        assertThat(sections.getTotalDistance()).isEqualTo(new Distance(13));
    }

    @DisplayName("구간이 하나 이하면 구간을 삭제할 수 없다")
    @Test
    void removeSectionInOnlyOneSection() {
        Sections targetSections = new Sections(new Section(이호선, 사당역, 강남역, 10));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> targetSections.removeSection(이호선, 사당역);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(ExistsOnlyOneSectionInLineException.class);
    }

    @DisplayName("추가하려는 구간이 기존 구간의 길이보다 길면 안된다.")
    @Test
    void outOfDistanceRange() {
        Station 선릉역 = new Station("선릉역");
        Section newSection = new Section(이호선, 강남역, 선릉역, 5);

        ThrowableAssert.ThrowingCallable throwingCallable = () -> sections.addSection(newSection);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(OutOfDistanceRangeException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    private List<String> getStationNames() {
        return sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}
