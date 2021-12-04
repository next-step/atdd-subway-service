package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SectionsTest
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@DataJpaTest
public class SectionsTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;


    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 마포역 = new Station("마포역");

    private final int 거리_5 = 5;
    private final int 거리_1 = 1;

    @Test
    @DisplayName("구간 추가 검증")
    public void addSection() {
        // given
        Line line = Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5);

        // when
        line.addSection(Section.of(line, 강남역, 마포역, Distance.of(1)));

        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getStations()).hasSize(3);
        assertThat(line.getStations()).containsExactly(강남역, 마포역, 광교역);
    }

    @Test
    @DisplayName("구간 삭제 검증")
    public void removeStation() {
        // given
        stations.saveAll(Arrays.asList(강남역, 마포역, 광교역));
        Line line = lines.save(Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5));
        line.addSection(Section.of(line, 마포역, 광교역, Distance.of(거리_1)));

        // when
        line.removeStation(마포역.getId());

        // then
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations()).containsExactly(강남역, 광교역);
    }
}
