package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 관련 도메인 테스트")
public class LineTest {

    private Station 강남역;
    private Station 양재역;
    private Station 양재시민의숲역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        양재시민의숲역 = createStation("양재시민의숲역");
        광교역 = createStation("광교역");
    }

    @DisplayName("노선 생성 시, 상행역이 비어있으면 에러가 발생한다.")
    @Test
    void createNewLineThrowErrorWhenUpStationIsNull() {
        // when
        assertThatThrownBy(() -> createLine("신분당선", "bg-red", null, 강남역, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("노선 생성 시, 하행역이 비어있으면 에러가 발생한다.")
    @Test
    void createNewLineThrowErrorWhenDownStationIsNull() {
        // when
        assertThatThrownBy(() -> createLine("신분당선", "bg-red", 강남역, null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.하행종착역은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("노선을 생성하면, 해당 노선 내에 존재하는 지하철역들을 조회할 수 있다.")
    @Test
    void createNewLine() {
        // when
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);

        // then
        assertThat(line.findStations()).hasSize(2);
    }

    @DisplayName("노선에 구역을 추가하면, 해당 노선 내에 존재하는 지하철역들을 상행부터 하행까지 정렬된 형태로 조회할 수 있다.")
    @Test
    void addSectionInLine() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        Section section = createSection(line, 양재역, 양재시민의숲역, 15);

        // when
        line.addSection(section);

        // then
        assertThat(line.findInOrderStations()).containsExactly(강남역, 양재역, 양재시민의숲역);
    }

    @DisplayName("노선명과 노선색상을 수정한다.")
    @Test
    void updateLineColorAndLineName() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);

        // when
        line.updateNameAndColor("2호선", "bg-green");

        // then
        assertAll(
                () -> assertThat(line.getColor()).isEqualTo(Color.from("bg-green")),
                () -> assertThat(line.getName()).isEqualTo(Name.from("2호선"))
        );
    }

    @DisplayName("노선명과 노선색상 수정 시, 노선명이 비어있으면 노선색상만 수정한다.")
    @Test
    void updateLineColor() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);

        // when
        line.updateNameAndColor(null, "bg-green");

        // then
        assertAll(
                () -> assertThat(line.getColor()).isEqualTo(Color.from("bg-green")),
                () -> assertThat(line.getName()).isEqualTo(Name.from("신분당선"))
        );
    }

    @DisplayName("노선명과 노선색상 수정 시, 노선색상이 비어있으면 노선명만 수정한다.")
    @Test
    void updateLineName() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);

        // when
        line.updateNameAndColor("2호선", null);

        // then
        assertAll(
                () -> assertThat(line.getColor()).isEqualTo(Color.from("bg-red")),
                () -> assertThat(line.getName()).isEqualTo(Name.from("2호선"))
        );
    }

    @DisplayName("노선에서 역을 삭제하면, 해당 노선 내에 존재하는 존재하는 지하철역들을 정렬하여 조회했을 때 해당 역이 조회되지 않는다.")
    @Test
    void removeStationInLine() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        line.addSection(createSection(line, 양재역, 양재시민의숲역, 15));

        // when
        line.removeStation(양재역);

        // then
        assertThat(line.findInOrderStations()).containsExactly(강남역, 양재시민의숲역);
    }

    @DisplayName("노선에 존재하지 않는 역을 삭제하면, 에러가 발생한다.")
    @Test
    void removeStationNotInLineThrowError() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        line.addSection(createSection(line, 양재역, 양재시민의숲역, 15));

        // when & then
        assertThatThrownBy(() -> line.removeStation(광교역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선_내_존재하지_않는_역.getErrorMessage());
    }

    @DisplayName("노선에 구간이 1개일 때, 역을 삭제하려고 하면 에러가 발생한다.")
    @Test
    void removeStationInOnlyOneSectionLineThrowError() {
        // given
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10);

        // when & then
        assertThatThrownBy(() -> line.removeStation(강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선에_속한_구간이_하나이면_제거_불가.getErrorMessage());
    }

    @DisplayName("노선에 추가 요금을 등록할 수 있다.")
    @Test
    void createLineWithLineFare() {
        // given
        int lineFare = 900;
        Line line = createLine("신분당선", "bg-red", 강남역, 양재역, 10, lineFare);

        // when & then
        assertThat(line.getLineFare()).isEqualTo(Fare.from(lineFare));
    }
}
