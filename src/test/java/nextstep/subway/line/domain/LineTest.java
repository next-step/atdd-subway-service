package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : LineTest
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@SpringBootTest
@ActiveProfiles("test")
public class LineTest {

    @Autowired
    LineRepository lines;

    @Autowired
    StationRepository stations;

    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final int 거리_5 = 5;

    @Test
    @DisplayName("Line 도메인으로 stations를 반환하기")
    public void create() {
        // when
        Line line = lines.save(new Line("신분당선", "빨강", 강남역, 광교역, 거리_5));

        // then
        assertThat(line.getStations()).containsExactly(강남역, 광교역);
    }

}
