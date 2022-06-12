package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.path.application.PathServiceTest.getLines;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("전체 노선에 대한 기능")
public class LinesTest {
    private Lines lines;

    @BeforeEach
    void setUp() {
        lines = new Lines(getLines());
    }

    @Test
    void 전체_노선에_등록된_역을_조회한다() {
        // when
        List<Station> stations =  lines.getAllStations();

        // then
        assertThat(stations).hasSize(4);
    }

    @Test
    void 전체_노선에_등록된_구간을_조회한다() {
        // when
        List<Section> sections = lines.getAllSections();

        // then
        assertThat(sections).hasSize(4);
    }
}
