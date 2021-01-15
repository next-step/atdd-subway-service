package nextstep.subway.line.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.line.exceptions.SectionsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathsTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 신림역;
    private List<Section> sections = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // station
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신림역 = new Station("신림역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(교대역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        // line
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        ReflectionTestUtils.setField(이호선, "id", 1L);
        ReflectionTestUtils.setField(신분당선, "id", 2L);
        ReflectionTestUtils.setField(삼호선, "id", 3L);

        // section
        Section section1 = 이호선.getSections().getSections().get(0);
        Section section2 = 신분당선.getSections().getSections().get(0);
        Section section3 = 삼호선.getSections().getSections().get(0);
        Section section4 = 삼호선.getSections().getSections().get(1);
        ReflectionTestUtils.setField(section1, "id", 1L);
        ReflectionTestUtils.setField(section2, "id", 2L);
        ReflectionTestUtils.setField(section3, "id", 3L);
        ReflectionTestUtils.setField(section4, "id", 4L);
        sections.add(section1);
        sections.add(section2);
        sections.add(section3);
        sections.add(section4);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getPath() {
        // when
        Paths paths = new Paths(sections);
        PathResponse response = paths.getShortestPath(강남역, 남부터미널역, new Fare());

        // then
        assertThat(response.getStations().size()).isEqualTo(3);
        assertThat(response.getDistance()).isEqualTo(12);
    }

    @DisplayName("최단 경로 조회 시 예외 - 출발역과 도착역이 같은 경우")
    @Test
    void getPathWithException1() {
        // then
        Paths paths = new Paths(sections);
        assertThatThrownBy(() -> {
            PathResponse response = paths.getShortestPath(강남역, 강남역, new Fare());
        }).isInstanceOf(SectionsException.class);
    }

    @DisplayName("최단 경로 조회 시 예외 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void getPathWithException2() {
        // when, then
        Paths paths = new Paths(sections);
        assertThatThrownBy(() -> {
            PathResponse response = paths.getShortestPath(교대역, 신림역, new Fare());
        }).isInstanceOf(SectionsException.class);
    }

    @DisplayName("최단 경로 조회 시 예외 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void getPathWithException3() {
        // then
        Paths paths = new Paths(sections);
        assertThatThrownBy(() -> {
            PathResponse response = paths.getShortestPath(강남역, new Station("청계산입구역"), new Fare());
        }).isInstanceOf(SectionsException.class);
    }
}
