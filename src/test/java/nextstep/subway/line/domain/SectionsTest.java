package nextstep.subway.line.domain;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.domain.LinesTest.DEFAULT_SHORTEST_DISTANCE;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
    }

    @Test
    @DisplayName("없는 역이면 StationNotExistException이 발생한다")
    void 없는_역이면_StationNotExistException이_발생한다() {
        Sections sections = new Sections();

        sections.add(new Section(null, 양재역, 정자역, 3));
        sections.add(new Section(null, 판교역, 정자역, 1));

        assertThatExceptionOfType(StationNotExistException.class)
                .isThrownBy(() -> sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 판교역, 강남역));
    }

    @Test
    @DisplayName("역끼리의 거리를 측정할 수 있다.")
    void 역끼리의_거리를_측정할_수_있다() {
        Sections sections = new Sections();

        sections.add(new Section(null, 양재역, 정자역, 3));
        sections.add(new Section(null, 판교역, 정자역, 1));

        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 양재역, 정자역))
                .isEqualTo(new Distance(3));
        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 판교역, 정자역))
                .isEqualTo(new Distance(1));
        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 양재역, 판교역))
                .isEqualTo(new Distance(2));

        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 정자역, 양재역))
                .isEqualTo(new Distance(3));
        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 정자역, 판교역))
                .isEqualTo(new Distance(1));
        assertThat(sections.calcDistanceBetween(DEFAULT_SHORTEST_DISTANCE, 판교역, 양재역))
                .isEqualTo(new Distance(2));
    }
}