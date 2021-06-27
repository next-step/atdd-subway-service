package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nextstep.subway.path.exception.NoConnectedStationsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

/**
 * PathFinder의 기능 테스트 코드 작성
 */
class PathFinderTest {

    private Station gangnam;
    private Station gyodae;
    private Station seocho;
    private Station sadang;
    private Station nakseongdae;
    private Station goter;
    private Station chongshin;
    private Station naebang;
    private Station gubanpo;
    private Station shinbanpo;
    private Station bangbae;
    private Station namsung;
    private Line line2;
    private Line line3;
    private Line line4;
    private Line line7;
    private Line line9;

    @BeforeEach
    void setUp() {
        gangnam = new Station("강남역");
        gyodae = new Station("교대역");
        seocho = new Station("서초역");
        bangbae = new Station("방배역");
        sadang = new Station("사당역");
        nakseongdae = new Station("낙성대역");
        goter = new Station("고속터미널역");
        chongshin = new Station("총신대입구역");
        naebang = new Station("내방역");
        gubanpo = new Station("구반포역");
        shinbanpo = new Station("신반포역");
        namsung = new Station("남성역");

        line2 = new Line("2호선", "green", gangnam, gyodae, 3);
        line2.addSection(new Section(line2, gyodae, seocho, 2));
        line2.addSection(new Section(line2, seocho, bangbae, 2));
        line2.addSection(new Section(line2, bangbae, sadang, 2));
        line2.addSection(new Section(line2, sadang, nakseongdae, 2));

        line3 = new Line("3호선", "orange", gyodae, goter, 2);
        line4 = new Line("4호선", "blue", sadang, chongshin, 3);
        line7 = new Line("7호선", "dark_green", namsung, chongshin, 1);
        line7.addSection(new Section(line7, chongshin, naebang, 5));
        line7.addSection(new Section(line7, naebang, goter, 3));

        line9 = new Line("7호선", "yellow", gubanpo, shinbanpo, 5);
    }

    @Test
    @DisplayName("환승역을 포함한 최단경로 조회 테스트")
    void find_shortestPath() {
        // given
        PathFinder finder = PathFinder.of(Arrays.asList(line2, line3, line4, line7));
        assertAll(
                () -> assertThat(finder.isConnectedPath(seocho, naebang)).isTrue(),
                () -> {
                    // when
                    Path path = finder.findPath(gangnam, namsung);

                    // then
                    List<String> pathStationNames = path.getStations()
                            .stream()
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    List<String> targetStationNames = Stream.of(gangnam, gyodae, seocho, bangbae, sadang, chongshin, namsung)
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    assertAll(
                            () -> assertThat(Arrays.equals(pathStationNames.toArray(), targetStationNames.toArray())).isTrue(),
                            () -> assertThat(path.getTotalDistance()).isEqualTo(13)
                    );
                },
                () -> {
                    // when
                    Path path = finder.findPath(seocho, naebang);

                    // then
                    List<String> pathStationNames = path.getStations()
                            .stream()
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    List<String> targetStationNames = Stream.of(seocho, gyodae, goter, naebang)
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    assertAll(
                            () -> assertThat(Arrays.equals(pathStationNames.toArray(), targetStationNames.toArray())).isTrue(),
                            () -> assertThat(path.getTotalDistance()).isEqualTo(7)
                    );
                }
        );
    }

    @TestFactory
    @DisplayName("예외처리")
    List<DynamicTest> same_stations_error() {
        // given
        PathFinder finder = PathFinder.of(Arrays.asList(line2, line9));

        return Arrays.asList(
                dynamicTest("연결되지 않은 출발역, 도착역 입력 시 오류", () -> assertAll(
                        () -> assertThat(finder.isConnectedPath(gangnam, shinbanpo)).isFalse(),
                        () -> assertThatThrownBy(() -> finder.findPath(gangnam, shinbanpo))
                                .isInstanceOf(NoConnectedStationsException.class)
                                .hasMessage("구간으로 연결되지 않은 역입니다.")
                )),
                dynamicTest("등록된 역이나 구간에 포함되지 않은 역 조회 시", () -> {
                    // given
                    Station nodle = new Station("노들역");

                    // then
                    assertAll(
                            () -> assertThat(finder.isConnectedPath(gangnam, nodle)).isFalse(),
                            () -> assertThatThrownBy(() -> finder.findPath(gangnam, nodle))
                                    .isInstanceOf(IllegalArgumentException.class)
                                    .hasMessage("도착역이 속하는 노선이 없습니다."),
                            () -> assertThatThrownBy(() -> finder.findPath(nodle, gangnam))
                                    .isInstanceOf(IllegalArgumentException.class)
                                    .hasMessage("출발역이 속하는 노선이 없습니다.")
                    );
                })
        );
    }
}
