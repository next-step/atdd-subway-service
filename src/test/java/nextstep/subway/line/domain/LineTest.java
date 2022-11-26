package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    private Station 마곡역;
    private Station 마포역;
    private Station 김포공항역;

    @BeforeEach
    void setUp() {
        마곡역 = new Station("마곡역");
        마포역 = new Station("마포역");
        김포공항역 = new Station("김포공항역");
    }

    @DisplayName("노선의 이름과 색상을 변경한다.")
    @Test
    void update() {
        Line line = new Line("5호선", "보라색", 마곡역, 마포역, 10);

        line.update("9호선", "금색");

        assertAll(
            () -> assertThat(line.getName()).isEqualTo(Name.from("9호선")),
            () -> assertThat(line.getColor()).isEqualTo(Color.from("금색"))
        );
    }

    @DisplayName("노선에 구간을 추가한다.")
    @Test
    void addSection() {
        Line line = new Line("5호선", "보라색", 마곡역, 마포역, 10);
        Section section = Section.of(line, 김포공항역, 마곡역, 10);

        line.addSection(section);

        assertThat(line.getStations()).hasSize(3)
            .containsExactly(김포공항역, 마곡역, 마포역);
    }
}
