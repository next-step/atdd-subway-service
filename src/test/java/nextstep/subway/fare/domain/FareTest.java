package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private List<Section> sectionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // station
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(교대역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        // line
        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        ReflectionTestUtils.setField(신분당선, "id", 2L);

        // section
        Section section2 = 신분당선.getSections().getSections().get(0);
        ReflectionTestUtils.setField(section2, "id", 2L);
        sectionList.add(section2);
    }

    @DisplayName("최단 경로의 요금을 조회한다.")
    @Test
    void calculateFare() {
        // given
        List<Station> paths = Arrays.asList(강남역, 양재역);
        List<Section> sections = Arrays.asList(sectionList.get(0));

        // when
        Fare fare = new Fare();
        fare.calculateFare(paths, sections,12);

        // then
        assertThat(fare.getFare()).isEqualTo(1350);
    }

    @DisplayName("노선별 추가 요금 정책을 적용한다.")
    @Test
    void calculateFareWithLine() {
        // given
        Line 이호선 = new Line("2호선", "green", 400);
        Line 삼호선 = new Line("3호선", "orange", 500);
        List<Section> sections = Arrays.asList(new Section(이호선, 강남역, 교대역, 10),
                new Section(삼호선, 교대역, 남부터미널역, 2));
        List<Station> paths = Arrays.asList(강남역, 교대역, 남부터미널역);

        // when
        Fare fare = new Fare();
        fare.calculateFare(paths, sections, 12);

        // then
        assertThat(fare.getFare()).isEqualTo(1850);
    }

    @DisplayName("연령별 요금 할인을 적용한다. - 어린이(운임에서 350원을 공제한 금액의 50%할인)")
    @Test
    void calculateFareWithAgeChild() {
        // given
        List<Station> paths = Arrays.asList(강남역, 양재역);
        List<Section> sections = Arrays.asList(sectionList.get(0));

        // when
        Fare fare = new Fare(AgePolicy.CHILD);
        fare.calculateFare(paths, sections,12);

        // then (1350-350)의 50프로 할인
        assertThat(fare.getFare()).isEqualTo(500);
    }

    @DisplayName("연령별 요금 할인을 적용한다. - 청소년(운임에서 350원을 공제한 금액의 20%할인)")
    @Test
    void calculateFareWithAgeTeenager() {
        // given
        List<Station> paths = Arrays.asList(강남역, 양재역);
        List<Section> sections = Arrays.asList(sectionList.get(0));

        // when
        Fare fare = new Fare(AgePolicy.TEENAGER);
        fare.calculateFare(paths, sections,12);

        // then (1350-350)의 20프로 할인
        assertThat(fare.getFare()).isEqualTo(800);
    }
}
