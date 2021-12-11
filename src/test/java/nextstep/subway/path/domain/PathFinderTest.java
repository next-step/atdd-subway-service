package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
    
    @Test
    @DisplayName("최단 경로 확인")
    void 최단_경로_확인() {
        // given
        Station 을지로4가 = Station.from("을지로4가");
        Station 동대문역사문화공원 = Station.from("동대문역사문화공원");
        Line 이호선 = Line.of("이호선", "bg-green-600", 을지로4가, 동대문역사문화공원, Distance.from(30));
        Line 오호선 = Line.of("오호선", "bg-purple-600", 을지로4가, 동대문역사문화공원, Distance.from(50));
        
        PathFinder pathFinder = PathFinder.of(Arrays.asList(이호선, 오호선));
        
        // then, when
        assertThat(pathFinder.findShortestDistance(을지로4가, 동대문역사문화공원)).isEqualTo(30);
    }
}
