package nextstep.subway.path.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Path 테스트")
class PathTest {

    @Test
    @DisplayName("시작역과 도착역이 같을때 예외처리")
    void sameStartAndEndStationTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");

        Line line = new Line("2호선", "green", dangsanStation, ehwaStation, 10);

        Section section = new Section(line, dangsanStation, hapJeongStation, 3);
        line.addSection(section);

        Assertions.assertThatThrownBy(() -> {
                    new Path(dangsanStation, dangsanStation);
                }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THERE_IS_SAME_STATIONS.errorMessage());
    }

}
