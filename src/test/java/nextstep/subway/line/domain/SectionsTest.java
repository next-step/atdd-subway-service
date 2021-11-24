package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 목록 테스트")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 사당역;
    private Station 방배역;
    private Line 이호선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        사당역 = new Station("사당역");
        방배역 = new Station("방배역");
        이호선 = new Line("2호선", "green");
        sections = new Sections();
    }

    @Test
    @DisplayName("상행 종점역을 반환한다.")
    void getUpStation() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));
        sections.addSection(이호선, 사당역, 강남역, new Distance(15));

        // when
        Station station = sections.findUpStation();

        // then
        assertThat(station).isEqualTo(사당역);
    }

    @Test
    @DisplayName("지하철역 목록을 반환한다.")
    void getStations() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));
        sections.addSection(이호선, 사당역, 강남역, new Distance(15));

        // when
        List<Station> stations = sections.findStations();

        // then
        assertThat(stations).containsExactly(사당역, 강남역, 역삼역);
    }

    @Test
    @DisplayName("지하철 노선에 구간을 추가한다.")
    void addSection() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));

        // when
        sections.addSection(이호선, 사당역, 강남역, new Distance(15));

        // then
        assertThat(sections).isEqualTo(new Sections(Arrays.asList(
                new Section(이호선, 강남역, 역삼역, new Distance(10)),
                new Section(이호선, 사당역, 강남역, new Distance(15))
        )));
    }

    @Test
    @DisplayName("존재하는 역으로 지하철 노선에 구간을 추가하면 예외를 발생한다.")
    void addSectionThrowException1() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));

        // when & then
        assertThatExceptionOfType(SectionAddFailedException.class)
                .isThrownBy(() -> sections.addSection(이호선, 강남역, 역삼역, new Distance(15)))
                .withMessageMatching("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 역으로 지하철 노선에 구간을 추가하면 예외를 발생한다.")
    void addSectionThrowException2() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));

        // when & then
        assertThatExceptionOfType(SectionAddFailedException.class)
                .isThrownBy(() -> sections.addSection(이호선, 사당역, 방배역, new Distance(15)))
                .withMessageMatching("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("지하철 노선에서 구간을 제거한다.")
    void remove() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));
        sections.addSection(이호선, 사당역, 강남역, new Distance(15));

        // when
        sections.remove(강남역);

        // then
        assertThat(sections).isEqualTo(new Sections(Collections.singletonList(
                new Section(이호선, 사당역, 역삼역, new Distance(25))
        )));
    }

    @Test
    @DisplayName("구간이 1개만 존재할 때 지하철 노선에서 구간을 제거하면 예외를 발생한다.")
    void removeThrowException() {
        // given
        sections.addSection(이호선, 강남역, 역삼역, new Distance(10));

        // when & then
        assertThatExceptionOfType(SectionRemoveFailedException.class)
                .isThrownBy(() -> sections.remove(강남역))
                .withMessageMatching("구간을 제거할 수 없습니다.");
    }
}
