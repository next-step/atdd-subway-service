package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.utils.FactoryMethods.*;
import static org.assertj.core.api.Assertions.*;

class LineTest {
    @Test
    @DisplayName("Line 수정")
    void updateLine(){
        //given
        Station 상행역 = createStation("상행역");
        Station 하행역 = createStation("하행역");
        Line line = createLine("1호선", "blue", 상행역, 하행역, 10);
        Line newLine = createLine("2호선", "green", 상행역, 하행역, 10);

        //when
        line.update(newLine);

        //then
        Assertions.assertAll(
                () -> assertThat(line.getName()).isEqualTo("2호선"),
                () -> assertThat(line.getColor()).isEqualTo("green")
        );
    }

    @Test
    @DisplayName("Line에 구간 추가")
    void addSection(){
        //given
        Station 상행역 = createStation("상행역");
        Station 하행역 = createStation("하행역");
        Line line = createLine("1호선", "blue", 상행역, 하행역, 10);

        //when
        Station 중간역 = createStation("중간역");
        Section section = createSection(중간역, 하행역, 5);
        line.addSection(section);

        //then
        assertThat(line.getStations()).containsExactly(상행역, 중간역, 하행역);
    }

    @Test
    @DisplayName("Line에서 역 제거")
    void removeStation(){
        //given
        Station 상행역 = createStation("상행역");
        Station 중간역 = createStation("중간역");
        Station 하행역 = createStation("하행역");
        Line line = createLine("1호선", "blue", 상행역, 하행역, 10);
        Section section = createSection(중간역, 하행역, 5);
        line.addSection(section);

        //when
        line.removeStation(상행역);

        //then
        assertThat(line.getStations()).containsExactly(중간역, 하행역);
    }
}