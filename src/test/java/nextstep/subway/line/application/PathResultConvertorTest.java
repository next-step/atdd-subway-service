package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathResultConvertorTest {
    Station station1;
    Station station2;
    @BeforeEach
    void beforeEach(){
        station1 = new Station("강남역");
        station2 = new Station("잠실역");
    }

    @Test
    @DisplayName("거리 추가 요금 계산")
    void convertDistanceTest(){
        // given
        Line line = new Line("2호선", "green", station1, station2, 20, 0);
        Set<Line> set = new HashSet<>();
        set.add(line);

        PathResult pathResult = new PathResult(Arrays.asList(
                station1, station2
        ), 20, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getCharge()).isEqualTo(1450);
    }

    @Test
    @DisplayName("노선 추가 요금 계산")
    void convertLineTest(){
        // given
        Line line = new Line("2호선", "green", station1, station2, 5, 100);
        Set<Line> set = new HashSet<>();
        set.add(line);

        PathResult pathResult = new PathResult(Arrays.asList(
                station1, station2
        ), 5, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getCharge()).isEqualTo(1350);
    }

    @Test
    @DisplayName("청소년 사용자 추가 요금 계산")
    void convertTeenagerAgeTest(){
        // given
        Line line = new Line("2호선", "green", station1, station2, 5, 0);
        Set<Line> set = new HashSet<>();
        set.add(line);
        LoginMember loginMember = new LoginMember(111L, "test@test.com", 18);
        PathResult pathResult = new PathResult(Arrays.asList(
                station1, station2
        ), 5, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult, loginMember);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getCharge()).isEqualTo(720);
    }

    @Test
    @DisplayName("어린이 사용자 추가 요금 계산")
    void convertChildrenAgeTest(){
        // given
        Line line = new Line("2호선", "green", station1, station2, 5, 0);
        Set<Line> set = new HashSet<>();
        set.add(line);
        LoginMember loginMember = new LoginMember(111L, "test@test.com", 8);
        PathResult pathResult = new PathResult(Arrays.asList(
                station1, station2
        ), 5, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult, loginMember);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getCharge()).isEqualTo(450);
    }
}
