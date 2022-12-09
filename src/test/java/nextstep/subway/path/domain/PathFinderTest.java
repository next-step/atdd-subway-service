package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.dto.PathBag;
import nextstep.subway.line.dto.SectionPath;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("경로 탐색 클래스 테스트")
class PathFinderTest {

    @DisplayName("생성 성공")
    @Test
    void create_pathFinder_success() {
        assertThatNoException().isThrownBy(PathFinder::new);
    }

    @DisplayName("최단 경로 탐색 성공")
    @Test
    void find_path_succes() {
        //given: [삼전역] -(10)-> [종합운동장역] -(10)-> [잠실새내역] -(10)-> [석촌고분역]
        PathFinder pathFinder = new PathFinder();
        Station 삼전역 = Station.from("삼전역");
        Station 종합운동장역 = Station.from("종합운동장역");
        Station 잠실새내역 = Station.from("잠실새내역");
        Station 석촌고분역 = Station.from("석촌고분역");
        final int 거리 = 10;
        PathBag pathBag = PathBag.from(Arrays.asList(
                SectionPath.of(삼전역, 종합운동장역, 거리, Money.zero()),
                SectionPath.of(종합운동장역, 잠실새내역, 거리, Money.zero()),
                SectionPath.of(잠실새내역, 석촌고분역, 거리, Money.zero())));

        //when:
        PathResult result = pathFinder.findShortestPath(pathBag, 삼전역, 석촌고분역);
        //then:
        assertThat(result.getStationNames()).containsSequence("삼전역", "종합운동장역", "잠실새내역", "석촌고분역");
        assertThat(result.getWeight()).isEqualTo(30d);
    }

    @DisplayName("연결되지 않은 경로 탐색")
    @Test
    void find_path_IllegalArgumentException() {
        //given: [삼전역] -(10)-> [종합운동장역] -(10)-> [잠실새내역]
        //given: [석촌고분역] -(10)-> [석촌역]
        PathFinder pathFinder = new PathFinder();
        Station 삼전역 = Station.from("삼전역");
        Station 종합운동장역 = Station.from("종합운동장역");
        Station 잠실새내역 = Station.from("잠실새내역");
        Station 석촌고분역 = Station.from("석촌고분역");
        Station 석촌역 = Station.from("석촌역");
        final int 거리 = 10;
        PathBag pathBag = PathBag.from(Arrays.asList(
                SectionPath.of(삼전역, 종합운동장역, 거리, Money.zero()),
                SectionPath.of(종합운동장역, 잠실새내역, 거리, Money.zero()),
                SectionPath.of(석촌고분역, 석촌역, 거리, Money.zero())));
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(pathBag, 삼전역, 석촌고분역));
    }
}
