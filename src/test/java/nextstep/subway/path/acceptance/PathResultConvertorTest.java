package nextstep.subway.path.acceptance;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathResultConvertor;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathResultConvertorTest {

    Set<Line> set = new HashSet<>();;
    Station station1;
    Station station2;
    Line line;
    @BeforeEach
    void beforeEach(){
        station1 = new Station("강남역");
        station2 = new Station("잠실역");
        line = new Line("2호선", "green", station1, station2, 20, 200);
        set.add(line);
    }

    @Test
    @DisplayName("거리, 노선 추가 요금 계산")
    void convertTest(){
        // given

        PathResult pathResult = new PathResult(Arrays.asList(
            station1, station2
        ), 20, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getCharge()).isEqualTo(1650);
    }

    @Test
    @DisplayName("거리, 노선, 로그인 사용자 추가 요금 계산")
    void convertTest2(){
        // given
        LoginMember loginMember = new LoginMember(111L, "test@test.com", 18);
        PathResult pathResult = new PathResult(Arrays.asList(
            station1, station2
        ), 20, set);

        // when
        PathResponse pathResponse = PathResultConvertor.convert(pathResult, loginMember);

        // then
        assertThat(pathResponse.getStations()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getCharge()).isEqualTo(1040);
    }
}
