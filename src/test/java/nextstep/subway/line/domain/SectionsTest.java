package nextstep.subway.line.domain;

import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("지하철 구간에 대한 테스트")
public class SectionsTest {
    private Sections sections;
    private Line 팔호선;

    @BeforeEach
    void setUp() {
        팔호선 = Line.builder()
                .name("팔호선")
                .color("pink")
                .upStation(StationFixtures.천호역)
                .downStation(StationFixtures.산성역)
                .distance(30)
                .build();

        Section 문정역_모란역 = Section.builder().line(팔호선)
                .upStation(StationFixtures.문정역)
                .downStation(StationFixtures.산성역)
                .distance(new Distance(10))
                .build();

        Section 천호역_잠실역 = Section.builder().line(팔호선)
                .upStation(StationFixtures.천호역)
                .downStation(StationFixtures.잠실역)
                .distance(new Distance(10))
                .build();

        Section 잠실역_문정역 = Section.builder().line(팔호선)
                .upStation(StationFixtures.잠실역)
                .downStation(StationFixtures.문정역)
                .distance(new Distance(10))
                .build();

        sections = new Sections(Arrays.asList(
                문정역_모란역,
                천호역_잠실역,
                잠실역_문정역
        ));
    }

    @DisplayName("지하철 구간을 상행 구간 기준으로 정렬")
    @Test
    void orderedStations() {
        // when
        List<Station> orderedStations = sections.getStations();

        // then
        assertThat(orderedStations).containsExactly(StationFixtures.천호역, StationFixtures.잠실역,
                        StationFixtures.문정역, StationFixtures.산성역);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 : 기존 상행역 - 새로운 하행역")
    @Test
    void addSectionExistUpStation() {
        // given
        Station 송파역 = new Station("송파역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(StationFixtures.잠실역)
                .downStation(송파역)
                .distance(new Distance(5))
                .build();

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.천호역,
                StationFixtures.잠실역, 송파역, StationFixtures.문정역, StationFixtures.산성역));
        assertThat(라인전체길이()).isEqualTo(30);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 : 새로운 상행역 - 기존 하행역")
    @Test
    void addSectionExistDownStation() {
        // given
        Station 송파역 = new Station("송파역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(송파역)
                .downStation(StationFixtures.문정역)
                .distance(new Distance(5))
                .build();

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.천호역,
                StationFixtures.잠실역, 송파역, StationFixtures.문정역, StationFixtures.산성역));
        assertThat(라인전체길이()).isEqualTo(30);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록할 경우")
    @Test
    void addSectionNewUpStation() {
        // given
        Station 암사역 = new Station("암사역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(암사역)
                .downStation(StationFixtures.천호역)
                .distance(new Distance(5))
                .build();

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(암사역, StationFixtures.천호역,
                StationFixtures.잠실역, StationFixtures.문정역, StationFixtures.산성역));
        assertThat(라인전체길이()).isEqualTo(35);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록할 경우")
    @Test
    void addSectionNewDownStation() {
        // given
        Station 모란역 = new Station("모란역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(StationFixtures.산성역)
                .downStation(모란역)
                .distance(new Distance(5))
                .build();

        // when
        sections.addSection(newSection);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.천호역, StationFixtures.잠실역,
                StationFixtures.문정역, StationFixtures.산성역, 모란역));
        assertThat(라인전체길이()).isEqualTo(35);
    }

    private int 라인전체길이() {
        return sections.getSections().stream()
                .map(section -> section.getDistance())
                .mapToInt(Distance::get)
                .sum();
    }

    @DisplayName("상행역 기준, 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    @ParameterizedTest
    @ValueSource(ints = {10, 30})
    void addInvalidDistanceBasedUpStation(int distance) {
        // given
        Station 송파역 = new Station("송파역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(StationFixtures.천호역)
                .downStation(송파역)
                .distance(new Distance(distance))
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.addSection(newSection);
        }).withMessageMatching("거리는 0 또는 음수가 될 수 없습니다.");
    }

    @DisplayName("하행역 기준, 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    @ParameterizedTest
    @ValueSource(ints = {10, 30})
    void addInvalidDistanceBasedDownStation(int distance) {
        // given
        Station 송파역 = new Station("송파역");
        Section newSection = Section.builder().line(팔호선)
                .upStation(송파역)
                .downStation(StationFixtures.산성역)
                .distance(new Distance(distance))
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.addSection(newSection);
        }).withMessageMatching("거리는 0 또는 음수가 될 수 없습니다.");
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    @Test
    void addSectionAlreadyExistStations() {
        // given
        Section newSection = Section.builder().line(팔호선)
                .upStation(StationFixtures.천호역)
                .downStation(StationFixtures.산성역)
                .distance(new Distance(10))
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.addSection(newSection);
        }).withMessageMatching("이미 등록된 구간 입니다.");
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void addNotExistStationsInLine() {
        // given
        Section newSection = Section.builder().line(팔호선)
                .upStation(new Station("암사역"))
                .downStation(new Station("모란역"))
                .distance(new Distance(10))
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.addSection(newSection);
        }).withMessageMatching("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("역과 역사이에 중간역 삭제")
    @Test
    void deleteMiddleSection() {
        // when
        sections.removeSection(팔호선, StationFixtures.잠실역);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.천호역,
                StationFixtures.문정역, StationFixtures.산성역));
        assertThat(라인전체길이()).isEqualTo(30);
    }

    @DisplayName("노선에서 상행 종점 제거")
    @Test
    void deleteStartSection() {
        // when
        sections.removeSection(팔호선, StationFixtures.천호역);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.잠실역,
                StationFixtures.문정역, StationFixtures.산성역));
        assertThat(라인전체길이()).isEqualTo(20);
    }

    @DisplayName("노선에서 하행 종점 제거")
    @Test
    void deleteEndSection() {
        // when
        sections.removeSection(팔호선, StationFixtures.산성역);

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(Arrays.asList(StationFixtures.천호역,
                StationFixtures.잠실역, StationFixtures.문정역));
        assertThat(라인전체길이()).isEqualTo(20);
    }

    @DisplayName("노선에 등록되지 않은 역 제거할 수 없음")
    @Test
    void deleteNotExistStations() {
        // given
        Station 송파역 = new Station("송파역");

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.removeSection(팔호선, 송파역);
        }).withMessageMatching("노선에 등록되지 않은 역은 제거할 수 없습니다.");
    }

    @DisplayName("등록된 구간이 1개일 때, 상행역을 제거할 수 없음")
    @Test
    void deleteStartStationOnlyOneSections() {
        // when
        sections.removeSection(팔호선, StationFixtures.잠실역);
        sections.removeSection(팔호선, StationFixtures.문정역);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.removeSection(팔호선, StationFixtures.천호역);
        }).withMessageMatching("구간이 하나인 지하철 노선은 구간을 제거할 수 없습니다.");
    }

    @DisplayName("등록된 구간이 1개일 때, 하행역을 제거할 수 없음")
    @Test
    void deleteEndStationOnlyOneSections() {
        // when
        sections.removeSection(팔호선, StationFixtures.잠실역);
        sections.removeSection(팔호선, StationFixtures.문정역);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            sections.removeSection(팔호선, StationFixtures.산성역);
        }).withMessageMatching("구간이 하나인 지하철 노선은 구간을 제거할 수 없습니다.");
    }
}
