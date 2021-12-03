package nextstep.subway.line.domain;

import nextstep.subway.common.exception.distance.IllegalDistanceException;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : LineTest
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
public class LineTest {
    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final int 거리_5 = 5;

    @Test
    @DisplayName("Line 도메인으로 stations를 반환하기")
    public void create() {
        // when
        Line line = Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 광교역);
    }

    @Test
    @DisplayName("Line response List 반환하기")
    public void findLines() {
        //given
        List<Line> lines = Arrays.asList(
                Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5),
                Line.of("8호선", "핑크", 광교역, 강남역, 거리_5)
        );

        // when
        List<LineResponse> lineResponse = LineResponse.ofList(lines);

        // then
        assertThat(lineResponse).hasSize(2);
    }

    @Test
    @DisplayName("유효한 거리 노선 객체 생성하기")
    public void validLine() {
        // when
        Line line = Line.of("2호선", "그린", 강남역, 광교역, Distance.MIN_DISTANCE);

        // then
        assertThat(line).isNotNull();
        assertThat(line.getSections().get(0).getDistance()).isEqualTo(Distance.of(1));
    }

    @Test
    @DisplayName("유효하지 않은 거리 노선 객체 생성하기")
    public void invalidLine() {
        assertThatThrownBy(() -> Line.of("2호선", "그린", 강남역, 광교역, Distance.MIN_DISTANCE -1))
                .isInstanceOf(IllegalDistanceException.class)
                .hasMessageContaining("거리는 1보다 작을 수 없습니다.");
    }

}
