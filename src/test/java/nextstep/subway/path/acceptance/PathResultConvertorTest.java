package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathResultConvertor;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathResultConvertorTest {

    @Test
    @DisplayName("PathResult 를 PathResponse 로 변환")
    void convertTest(){
        // given
        Station station1 = new Station("강남역");
        Station station2 = new Station("잠실역");
        Line line = new Line("2호선", "green", station1, station2, 20, 200);
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
        assertThat(pathResponse.getCharge()).isEqualTo(1650);
    }
}
