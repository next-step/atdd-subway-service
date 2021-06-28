package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.farePolicy.KidsDiscountPolicy;
import nextstep.subway.path.farePolicy.TeenagersDiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {
    private Station 강남역;
    private Station 선릉역;
    private Line line;

    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");

        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(5)
                , new Fare(900));

    }

    @DisplayName("라인 생성시 경로 추가")
    @Test
    void createLine() {
        //given
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        //when
        List<StationResponse> shortestPath = pathResponse.getStations();
        int distance = pathResponse.getDistance();
        //then
        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.size()).isEqualTo(2);
        assertThat(shortestPath.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(shortestPath.get(1).getName()).isEqualTo(선릉역.getName());
        assertThat(distance).isEqualTo(5);
    }

    @DisplayName("구간 추가시 경로 추가")
    @Test
    void addSection() {
        //given
        Station 교대역 = new Station("교대역");
        line.addSection(선릉역, 교대역, new Distance(10));
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        //when
        PathResponse pathResponse = path.findShortestPath(강남역, 교대역);
        List<StationResponse> shortestPath = pathResponse.getStations();
        int distance = pathResponse.getDistance();
        //then
        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(shortestPath.get(1).getName()).isEqualTo(선릉역.getName());
        assertThat(shortestPath.get(2).getName()).isEqualTo(교대역.getName());
        assertThat(distance).isEqualTo(15);
    }

    @DisplayName("구간 삭제 후 경로 제거")
    @Test
    void removeSection() {
        //given
        Station 교대역 = new Station("교대역");
        line.addSection(선릉역, 교대역, new Distance(10));
        line.removeStation(교대역);
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        //when
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        List<StationResponse> shortestPath = pathResponse.getStations();
        int distance = pathResponse.getDistance();
        //then
        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.size()).isEqualTo(2);
        assertThat(shortestPath.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(shortestPath.get(1).getName()).isEqualTo(선릉역.getName());
        assertThat(distance).isEqualTo(5);
    }

    @DisplayName("구간 요금")
    @Test
    void fare() {
        // given
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        // when
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        // then
        assertThat(pathResponse.getFare()).isEqualTo(2150);
    }

    @DisplayName("구간 요금")
    @Test
    void overFareNotLineFare() {
        // given
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        // when
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        // then
        assertThat(pathResponse.getFare()).isEqualTo(2150);
    }

    @DisplayName("구간 추가 요금 - 10km 초과")
    @Test
    void overFare() {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(12)
                , new Fare(900));
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        // when
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        // then
        assertThat(pathResponse.getFare()).isEqualTo(2250);
    }

    @DisplayName("구간 추가 요금 - 50km 초과")
    @Test
    void overFareOtherDistance() {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(58)
                , new Fare());
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        // when
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역);
        // then
        assertThat(pathResponse.getFare()).isEqualTo(2150);
    }

    @DisplayName("구간 요금 할인 - 어린이")
    @Test
    void discountKids() {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(5)
                , new Fare(0));
        // when
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역, new KidsDiscountPolicy());
        // then
        assertThat(pathResponse.getFare()).isEqualTo(450);
    }

    @DisplayName("구간 요금 할인 - 어린이")
    @ParameterizedTest
    @CsvSource({"5, 450", "82, 1050", "15, 500"})
    void discountKids(int distance, int fare) {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(distance)
                , new Fare(0));
        // when
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역, new KidsDiscountPolicy());
        // then
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    @DisplayName("구간 요금 할인 - 어린이")
    @Test
    void discountKidsFare() {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(15)
                , new Fare(900));
        // when
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역, new KidsDiscountPolicy());
        // then
        assertThat(pathResponse.getFare()).isEqualTo(950);
    }

    @DisplayName("구간 요금 할인 - 청소년")
    @ParameterizedTest
    @CsvSource({"5, 720", "82, 1680"})
    void discountTeenager(int distance, int fare) {
        // given
        line = new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(distance)
                , new Fare(0));
        // when
        Path path = Path.of(new ArrayList<>(Arrays.asList(line)));
        PathResponse pathResponse = path.findShortestPath(강남역, 선릉역, new TeenagersDiscountPolicy());
        // then
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}