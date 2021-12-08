package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@DisplayName("구간들 관련 기능")
class SectionsTest {

    Station 강남역;
    Station 양재역;
    Station 양재시민의숲;
    Station 청계산입구;

    Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        양재시민의숲 = Station.from("양재시민의숲");
        청계산입구 = Station.from("청계산입구");

        신분당선 = Line.of("신분당선", "red");
    }

    @Test
    void 구간들_생성() {
        // given - when
        Sections actual = Sections.of(
                Section.of(신분당선, 강남역, 양재역, 10),
                Section.of(신분당선, 양재역, 양재시민의숲, 10),
                Section.of(신분당선, 양재시민의숲, 청계산입구, 10)
        );

        // then
        Assertions.assertThat(actual.getSections()).hasSize(3);
    }

    @Test
    void 구간들의_지하철역을_정렬하여_조회한다() {
        // given
        Sections sections = Sections.of(
                Section.of(신분당선, 강남역, 양재역, 10),
                Section.of(신분당선, 양재역, 양재시민의숲, 10),
                Section.of(신분당선, 양재시민의숲, 청계산입구, 10)
        );

        // when
        List<Station> actual = sections.getOrderedStations();

        // then
        Assertions.assertThat(actual)
                .hasSize(4)
                .containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 양재시민의숲, 청계산입구));
    }
}
