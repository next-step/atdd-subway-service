package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SectionsTest
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@SpringBootTest
@ActiveProfiles("test")
public class SectionsTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 마포역 = new Station("마포역");

    private final int 거리_5 = 5;
    private final int 거리_1 = 1;

    @Test
    @DisplayName("구간 추가 검증")
    public void addSection() {
        // given
        Line line = new Line("신분당선", "빨강", 강남역, 광교역, 거리_5);

        // when
        line.addSection(강남역, 마포역, 1);

        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getStations()).hasSize(3);
        assertThat(line.getStations()).containsExactly(강남역, 마포역, 광교역);
    }

    @Test
    @DisplayName("구간 삭제 검증")
    public void removeStation() {
        // given
        Line line = new Line("신분당선", "빨강", 강남역, 광교역, 거리_5);
        line.addSection(강남역, 마포역, 1);

        // when
        line.removeStation(광교역);

        // then
        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations()).containsExactly(강남역, 마포역);
    }
}
