package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.exception.ImpossibleDeleteException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {
    @DisplayName("구간들 길이(합) 테스트")
    @Test
    void getDistance() {
        List<Section> sections = new ArrayList<>();
        sections.add(createSection(노선_생성(), createStation(1L, "강남역"), createStation(2L, "양재역"),
                Distance.valueOf(10)));
        sections.add(createSection(노선_생성(), createStation(2L, "양재역"), createStation(4L, "양재시민의숲역"),
                Distance.valueOf(5)));
        assertThat(Sections.valueOf(sections).distance()).isEqualTo(Distance.valueOf(15));
    }

    @DisplayName("구간들 사이에 새로운 구간 등록 테스트 (상행역 일치)")
    @Test
    void addSectionIfEqualUpStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(2L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "판교역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들 사이에 새로운 구간 등록 테스트 (하행역 일치)")
    @Test
    void addSectionIfEqualDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(2L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(3L, "판교역"), createStation(2L, "양재시민의숲역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 하행 종점에 구간 추가")
    @Test
    void addSectionAtDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "판교역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(3L, "판교역"), createStation(2L, "양재시민의숲역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 상행 종점에 구간 추가")
    @Test
    void addSectionAtUpStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(2L, "판교역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(1L, "양재역"), createStation(2L, "판교역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 있는 구간 사이에 구간 길이가 같거나 큰 구간 추가하면 예외 발생")
    @ParameterizedTest(name = "구간들에 있는 하행역이 일치하는 구간 사이에 {0}의 구간길이가 같거나 큰 구간 추가하면 예외 발생")
    @ValueSource(ints = {10, 12})
    void addSectionInsideByEqualOrLongerDistance(int input) {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(1L, "양재역"), createStation(2L, "판교역"),
                Distance.valueOf(input));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("구간들에 구간 등록시 상행역과 하행역이 이미 구간들에 모두 등록되어 있는 경우 예외")
    @Test
    void addSectionContainsUpStationAndDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                Distance.valueOf(5));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간들에 구간 등록시 상행역과 하행역 둘 중 하나도 구간들에 포함되어있지 않으면 예외 발생")
    @Test
    void addSectionContainsNoneOfUpStationAndDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), createStation(2L, "판교"), createStation(4L, "미정"),
                Distance.valueOf(5));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("종점(상행)을 제거하면 구간을 제거하고 다음으로 오던 역이 종점이 된다.")
    @Test
    void deleteSectionFirst() {
        Station 양재역 = createStation(1L, "양재역");
        Station 판교역 = createStation(2L, "판교역");
        Station 양재시민의숲역 = createStation(3L, "양재시민의숲역");
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), 판교역, 양재시민의숲역,
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), 양재역, 판교역,
                Distance.valueOf(5));
        sections.addSection(newSection);

        sections.deleteSection(양재시민의숲역);
        assertAll(
                () -> assertThat(sections.orderedStations()).containsExactly(양재역, 판교역),
                () -> assertThat(sections.distance().distance()).isEqualTo(5)
        );
    }

    @DisplayName("중간역을 제거하면 중간역이 제거되고 재배치가 된다.")
    @Test
    void deleteSectionInSide() {
        Station 양재역 = createStation(1L, "양재역");
        Station 판교역 = createStation(2L, "판교역");
        Station 양재시민의숲역 = createStation(3L, "양재시민의숲역");
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), 판교역, 양재시민의숲역,
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), 양재역, 판교역,
                Distance.valueOf(5));
        sections.addSection(newSection);

        sections.deleteSection(판교역);
        assertAll(
                () -> assertThat(sections.orderedStations()).containsExactly(양재역, 양재시민의숲역),
                () -> assertThat(sections.distance().distance()).isEqualTo(15)
        );
    }

    @DisplayName("구간들에 없는 역을 제거하면 구간 제거에 실패한다.")
    @Test
    void deleteSectionNotAdded() {
        Station 양재역 = createStation(1L, "양재역");
        Station 판교역 = createStation(2L, "판교역");
        Station 양재시민의숲역 = createStation(3L, "양재시민의숲역");
        Station 미정역 = createStation(4L, "미정역");
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), 판교역, 양재시민의숲역,
                        Distance.valueOf(10))));
        Section newSection = createSection(노선_생성(), 양재역, 판교역,
                Distance.valueOf(5));
        sections.addSection(newSection);

        assertThatThrownBy(() -> sections.deleteSection(미정역))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("일치하는 상행역이 없습니다.");
    }

    @DisplayName("구간이 하나인 구간들에서 마지막 구간을 제거하면 구간 제거에 실패한다.")
    @Test
    void deleteSectionLeftAlone() {
        Station 양재역 = createStation(1L, "양재역");
        Station 판교역 = createStation(2L, "판교역");
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(노선_생성(), 양재역, 판교역,
                        Distance.valueOf(10))));
        assertThatThrownBy(() -> sections.deleteSection(판교역))
                .isInstanceOf(ImpossibleDeleteException.class)
                .hasMessage("제거 가능한 구간이 없습니다.");
    }

    @DisplayName("그래프에 간선 등록 테스트")
    @Test
    void makeEdgeWeightOn() {
        Station 양재역 = createStation(1L, "양재역");
        Station 양재시민의숲역 = createStation(2L, "양재시민의숲역");
        Station 판교역 = createStation(3L, "판교역");
        Section section = createSection(1L, 노선_생성(), 양재역, 판교역, Distance.valueOf(10));
        Sections sections = Sections.valueOf(Lists.newArrayList(section));
        Section newSection = createSection(2L, 노선_생성(), 판교역, 양재시민의숲역, Distance.valueOf(5));
        sections.addSection(newSection);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        sections.makeVertexOn(graph);
        sections.makeEdgeWeightOn(graph);
        assertAll(
                () -> assertThat(graph.getEdgeSource(graph.getEdge(양재역, 판교역))).isEqualTo(양재역),
                () -> assertThat(graph.getEdgeTarget(graph.getEdge(판교역, 양재시민의숲역))).isEqualTo(양재시민의숲역)
        );
    }

    private Line 노선_생성() {
        return Line.builder("노선", "color", createStation("지하철역"), createStation("새로운지하철역"), Distance.valueOf(1))
                .build();
    }
}
