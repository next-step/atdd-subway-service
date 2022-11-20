package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    @DisplayName("구역에 존재하는 지하철역들을 조회한다.")
    @Test
    void findStationInSection() {
        //given
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section = createSection(line, 양재역, 양재시민의숲역, 15);

        //when
        List<Station> stations = section.stations();

        //then
        assertThat(stations).contains(양재역, 양재시민의숲역);
    }

    @DisplayName("구역 생성 시, 노선 정보가 없으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenLineIsNull() {
        //when & then
        assertThatThrownBy(() -> createSection(null, createStation("강남역"), createStation("양재역"), 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선_정보가_없음.getErrorMessage());
    }

    @DisplayName("구역 생성 시, 상행역과 하행역이 동일하면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenEqualStations() {
        //given
        Station 강남역 = createStation("강남역");
        Line line = createLine("신분당선", "bg-red", 강남역, createStation("양재역"), 10);

        //when & then
        assertThatThrownBy(() -> createSection(line, 강남역, 강남역, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.구간의_상행역과_하행역이_동일할_수_없음.getErrorMessage());
    }

    @DisplayName("구역 생성 시, 상행역이 비어있으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenUpStationsIsNull() {
        //given
        Station 강남역 = createStation("강남역");
        Line line = createLine("신분당선", "bg-red", 강남역, createStation("양재역"), 10);

        //when & then
        assertThatThrownBy(() -> createSection(line, null, createStation("강남역"), 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("구역 생성 시, 하행역이 비어있으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenDownStationsIsNull() {
        //given
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);

        //when & then
        assertThatThrownBy(() -> createSection(line, 양재역, null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.하행종착역은_비어있을_수_없음.getErrorMessage());
    }
}
