package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    private Station 마곡역;
    private Station 마포역;
    private Station 김포공항역;
    private Station 공덕역;
    private Line 지하철5호선;

    @BeforeEach
    void setUp() {
        마곡역 = new Station("마곡역");
        마포역 = new Station("마포역");
        공덕역 = new Station("공덕역");
        김포공항역 = new Station("김포공항역");
        지하철5호선 = new Line("5호선", "bg-purple", 마곡역, 마포역, 10);
    }

    @DisplayName("구간에 존재하는 상행선, 하행선을 조회한다.")
    @Test
    void getStations() {
        Section section = Section.of(지하철5호선, 마곡역, 마포역, 10);

        List<Station> stations = section.getStations();

        assertThat(stations).hasSize(2)
            .containsExactly(마곡역, 마포역);
    }

    @DisplayName("상행선을 변경한다.")
    @Test
    void updateUpStation() {
        Section section1 = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Section section2 = Section.of(지하철5호선, 마곡역, 공덕역, 5);

        section1.updateStation(section2);

        assertThat(section1.getUpStation()).isEqualTo(공덕역);
    }

    @DisplayName("하행선을 변경한다.")
    @Test
    void updateDownStation() {
        Section section1 = Section.of(지하철5호선, 마곡역, 마포역, 10);
        Section section2 = Section.of(지하철5호선, 김포공항역, 마포역, 5);

        section1.updateStation(section2);

        assertThat(section1.getDownStation()).isEqualTo(김포공항역);
    }
}
