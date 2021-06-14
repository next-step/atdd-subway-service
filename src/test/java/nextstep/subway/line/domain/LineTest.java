package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LineTest {
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
    @DisplayName("섞여있어도 정렬하여 가져올 수 있다.")
    void 섞여있어도_정렬하여_가져올_수_있다() {
        Line line = new Line("신분당", "RED", 양재역, 판교역, 3);

        line.addSection(new Section(판교역, 정자역, new Distance(3)));
        line.addSection(new Section(강남역, 양재역, new Distance(3)));

        List<Station> stations = line.sortedStation()
                .toCollection();

        // then
        assertThat(stations)
                .containsExactly(강남역, 양재역, 판교역, 정자역);
    }

    @Test
    @DisplayName("삭제시 구간이 1개만 있으면 RuntimeException이 발생한다")
    void 삭제시_구간이_1개만_있으면_RuntimeException이_발생한다() {
        Line line = new Line("신분당", "RED", 강남역, 양재역, 3);

        // when / then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.removeStation(양재역));
    }

    @Test
    @DisplayName("역을 삭제하면 새로운 구간이 반환된다")
    void 역을_삭제하면_새로운_구간이_반환된다() {
        // given
        Line line = new Line("신분당", "RED", 강남역, 양재역, 3);
        line.addSection(new Section(양재역, 판교역, new Distance(5)));

        // when
        line.removeStation(양재역);

        // then
        assertThat(line.sortedStation().toCollection())
                .containsExactly(강남역, 판교역);
    }

    @Test
    @DisplayName("이미 등록된 역들을 등록하면 RuntimeException이 발생한다")
    void 이미_등록된_역들을_등록하면_RuntimeException이_발생한다() {
        // given
        Line line = new Line("신분당", "RED", 강남역, 양재역, 3);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addSection(new Section(강남역, 양재역, new Distance(3))))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("노선에 등록되지 않은 역을 연결하려 할 경우 RuntimeException이 발생한다")
    void 노선에_등록되지_않은_역을_연결하려_할_경우_RuntimeException이_발생한다() {
        // given
        Line line = new Line("신분당", "RED", 강남역, 양재역, 3);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addSection(new Section(판교역, 정자역, new Distance(3))))
                .withMessage("등록할 수 없는 구간 입니다.");
    }



    @Test
    @DisplayName("상행선에 연결할 수 있다")
    void 상행선에_연결할_수_있다() {
        // given
        Line line = new Line("신분당", "RED", 양재역, 판교역, 3);

        // when
        line.addSection(new Section(강남역, 양재역, new Distance(3)));

        // then

        assertThat(new StationResponses(line.sortedStation()).toCollection())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 양재역.getId(), 판교역.getId());
    }

    @Test
    @DisplayName("하행선에 연결할 수 있다")
    void 하행선에_연결할_수_있다() {
        // given
        Line line = new Line("신분당", "RED", 양재역, 판교역, 3);

        // when
        line.addSection(new Section(판교역, 정자역, new Distance(3)));

        // then

        assertThat(new StationResponses(line.sortedStation()).toCollection())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 양재역.getId(), 판교역.getId());
    }

    @Test
    @DisplayName("신규 노선이 하행성을 넘으면 RuntimeException이 발생한다")
    void 신규_노선이_하행성을_넘으면_RuntimeException이_발생한다() {
        // given
        Line line = new Line("신분당", "RED", 양재역, 판교역, 3);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addSection(new Section(양재역, 정자역, new Distance(5))))
                .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("신규 노선이 상행성을 넘으면 RuntimeException이 발생한다")
    void 신규_노선이_상행성을_넘으면_RuntimeException이_발생한다() {
        // given
        Line line = new Line("신분당", "RED", 판교역, 정자역, 3);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addSection(new Section(양재역, 정자역, new Distance(5))))
                .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("신규 노선이 하행성을 넘지 않으면 사이에 정상 등록된다")
    void 신규_노선이_하행성을_넘지_않으면_사이에_정상_등록된다() {
        // given
        Line line = new Line("신분당", "RED", 판교역, 정자역, 3);

        // when
        line.addSection(new Section(양재역, 판교역, new Distance(1)));

        // then
        assertThat(new StationResponses(line.sortedStation()).toCollection())
                .map(StationResponse::getId)
                .containsExactly(양재역.getId(), 판교역.getId(), 정자역.getId());
    }

    @Test
    @DisplayName("신규 노선이 상행성을 넘지 않으면 사이에 정상 등록된다")
    void 신규_노선이_상행성을_넘지_않으면_사이에_정상_등록된다() {
        // given
        Line line = new Line("신분당", "RED", 양재역, 정자역, 3);

        // when
        line.addSection(new Section(판교역, 정자역, new Distance(1)));

        // then
        assertThat(new StationResponses(line.sortedStation()).toCollection())
                .map(StationResponse::getId)
                .containsExactly(양재역.getId(), 판교역.getId(), 정자역.getId());
    }

    @Test
    @DisplayName("라인에 역이 존재하는지 확인이 가능하다")
    void 라인에_역이_존재하는지_확인이_가능하다() {
        Line line = new Line("신분당", "RED", 양재역, 정자역, 3);

        line.addSection(new Section(판교역, 정자역, new Distance(1)));

        assertThat(line.containsStation(양재역))
                .isTrue();
        assertThat(line.containsStation(정자역))
                .isTrue();
        assertThat(line.containsStation(판교역))
                .isTrue();
        assertThat(line.containsStation(강남역))
                .isFalse();
    }

    @Test
    void containsStationsExactly() {
        Line line = new Line("신분당", "RED", 양재역, 정자역, 3);

        line.addSection(new Section(판교역, 정자역, new Distance(1)));

        assertThat(line.containsStationsExactly(양재역, 정자역, 판교역))
                .isTrue();

        assertThat(line.containsStationsExactly(양재역, 정자역, 판교역, 강남역))
                .isFalse();
    }

    @Test
    @DisplayName("없는 역이면 IllegalArgumentException이 발생한다")
    void 없는_역이면_IllegalArgumentException이_발생한다() {
        Line line = new Line("신분당", "RED", 양재역, 정자역, 3);

        line.addSection(new Section(판교역, 정자역, new Distance(1)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> line.calcDistanceBetween(판교역, 강남역));
    }

    @Test
    @DisplayName("역끼리의 거리를 측정할 수 있다.")
    void 역끼리의_거리를_측정할_수_있다() {
        Line line = new Line("신분당", "RED", 양재역, 정자역, 3);

        line.addSection(new Section(판교역, 정자역, new Distance(1)));

        assertThat(line.calcDistanceBetween(양재역, 정자역))
                .isEqualTo(new Distance(3));
        assertThat(line.calcDistanceBetween(판교역, 정자역))
                .isEqualTo(new Distance(1));
        assertThat(line.calcDistanceBetween(양재역, 판교역))
                .isEqualTo(new Distance(2));

        assertThat(line.calcDistanceBetween(정자역, 양재역))
                .isEqualTo(new Distance(3));
        assertThat(line.calcDistanceBetween(정자역, 판교역))
                .isEqualTo(new Distance(1));
        assertThat(line.calcDistanceBetween(판교역, 양재역))
                .isEqualTo(new Distance(2));
    }
}