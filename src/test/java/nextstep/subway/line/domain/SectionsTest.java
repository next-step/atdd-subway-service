package nextstep.subway.line.domain;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.domain.LineTest.신분당선;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Sections sections = new Sections();

    @BeforeEach
    void beforeEach() {
        sections.add(new Section(신분당선, 역삼역, 양재역, 10));
        sections.add(new Section(신분당선, 양재역, 사당역, 5));
    }

    @DisplayName("구간 추가")
    @Test
    void addSection() {
        Sections actual = new Sections();
        actual.add(new Section(신분당선, 역삼역, 양재역, 10));
        actual.add(new Section(신분당선, 양재역, 사당역, 5));

        assertThat(actual).isEqualTo(sections);
    }

    @DisplayName("두 지하철역 중 하나는 등록 되어 있어야 합니다.")
    @Test
    void notIncludeOneStationExceptionTest() {
        Station 잠실새내 = new Station("잠실새내");
        Section section = new Section(신분당선, 잠실역, 잠실새내, 3);

        assertThatThrownBy(() -> sections.add(section)).hasMessage("두 지하철역 중 하나는 등록 되어 있어야 합니다.");
    }

    @DisplayName("이미 등록된 구간 입니다.")
    @Test
    void alreadyAddSectionExceptionTest() {
        Section section = new Section(신분당선, 역삼역, 사당역, 3);

        assertThatThrownBy(() -> sections.add(section)).hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("지하철 구간에서 역 삭제 - 중간역")
    @Test
    void removeMiddleStationTest() {
        sections.removeByStation(양재역);

        assertThat(sections.getStations()).isEqualTo(new Stations(Arrays.asList(역삼역, 사당역)));
    }

    @DisplayName("지하철 구간에서 역 삭제 - 상행종점")
    @Test
    void removeFirstStationTest() {
        sections.removeByStation(역삼역);

        assertThat(sections.getStations()).isEqualTo(new Stations(Arrays.asList(양재역, 사당역)));
    }

    @DisplayName("지하철 구간에서 역 삭제 - 하행종점")
    @Test
    void removeLastStationTest() {
        sections.removeByStation(사당역);

        assertThat(sections.getStations()).isEqualTo(new Stations(Arrays.asList(역삼역, 양재역)));
    }

    @DisplayName("구간이 하나 이하인 노선은 제거할 수 없습니다.")
    @Test
    void validateRemovableSizeExceptionTest() {
        sections.removeByStation(양재역);

        assertThatThrownBy(() -> sections.removeByStation(사당역)).hasMessage("구간이 하나 이하인 노선은 제거할 수 없습니다.");
    }

    @DisplayName("최단 경로 구하기")
    @Test
    void generatePathsTest() {
        PathResponse pathResponse = sections.generatePaths(역삼역, 사당역);
        List<StationResponse> expected = Stream.of(역삼역, 양재역, 사당역)
                .map(StationResponse::of)
                .collect(Collectors.toList());
        assertThat(pathResponse.getStations()).isEqualTo(expected);
        assertThat(pathResponse.getDistance()).isEqualTo(15);
    }
}