package nextstep.subway.path.domain;

import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.path.infrastructure.JGraphPathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.path.application
 * fileName : PathFinderTest
 * author : haedoang
 * date : 2021/12/05
 * description : PathFinder Test
 * <p>
 * 왕십리(start)   ←-------------- (20) ---------------→ 응봉 ←-------------- (20) ---------------→ 옥수          ## 40
 * ↕(2)                                                                                      	 ↘(3)
 * 한양대                                                                                            압구정
 * ↕(2)                                                                                 	         ↘(3)
 * 뚝섬                                                                                                 신사
 * ↕(2)                                                                            	                    ↘(3)
 * 성수                                                                                                     잠원
 * ↕(2)  ## 8                                                                                              ↘(3)   ## 12 + 40 => 52 (7개역)
 * 건대입구  ←- (7) -→ 뚝섬유원지 ←- (7) -→ 청담 ←- (7) -→ 강남구청 ←- (7) -→ 학동 ←- (7) -→  논현 ←- (7) -→ 반포 ←- (7) -→ 고속터미널(end)  ## 49 + 8 = 57
 */
@DataJpaTest
class PathFinderTest {
    private Station 왕십리_START;
    private Station 고속터미널_END;
    private Station 경로없는역;
    private Member 사용자;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        int DISTANCE_이호선 = 2;
        int DISTANCE_경의중앙선 = 20;
        int DISTANCE_삼호선 = 3;
        int DISTANCE_칠호선 = 7;

        사용자 = memberRepository.save(new Member("haedoang@gmail.com", "12", 33));

        //2호선
        Station 성수 = new Station("성수");
        Station 뚝섬 = new Station("뚝섬");
        Station 한양대 = new Station("한양대");
        왕십리_START = new Station("왕십리");

        //경의중앙
        Station 응봉 = new Station("응봉");
        Station 옥수 = new Station("옥수");

        //3호선
        Station 압구정 = new Station("압구정");
        Station 신사 = new Station("신사");
        Station 잠원 = new Station("잠원");
        고속터미널_END = new Station("고속터미널");

        //7호선
        Station 건대입구 = new Station("건대입구");
        Station 뚝섬유원지 = new Station("뚝섬유원지");
        Station 청담 = new Station("청담");
        Station 강남구청 = new Station("강남구청");
        Station 학동 = new Station("학동");
        Station 논현 = new Station("논현");
        Station 반포 = new Station("반포");

        //경로없는역
        경로없는역 = new Station("경로없는역");

        stationRepository.saveAll(
                Arrays.asList(
                        성수, 뚝섬, 왕십리_START, 한양대, 응봉, 옥수, 압구정, 신사, 잠원, 고속터미널_END, 건대입구, 뚝섬유원지, 청담, 강남구청, 학동, 논현, 반포, 경로없는역));

        Line 이호선 = lineRepository.save(Line.of("2호선", "그린", 왕십리_START, 한양대, DISTANCE_이호선));
        이호선.addSection(Section.of(이호선, 한양대, 뚝섬, Distance.of(DISTANCE_이호선)));
        이호선.addSection(Section.of(이호선, 뚝섬, 성수, Distance.of(DISTANCE_이호선)));
        이호선.addSection(Section.of(이호선, 성수, 건대입구, Distance.of(DISTANCE_이호선)));

        Line 칠호선 = lineRepository.save(Line.of("7호선", "카고", 건대입구, 뚝섬유원지, DISTANCE_칠호선));
        칠호선.addSection(Section.of(칠호선, 뚝섬유원지, 청담, Distance.of(DISTANCE_칠호선)));
        칠호선.addSection(Section.of(칠호선, 청담, 강남구청, Distance.of(DISTANCE_칠호선)));
        칠호선.addSection(Section.of(칠호선, 강남구청, 학동, Distance.of(DISTANCE_칠호선)));
        칠호선.addSection(Section.of(칠호선, 학동, 논현, Distance.of(DISTANCE_칠호선)));
        칠호선.addSection(Section.of(칠호선, 논현, 반포, Distance.of(DISTANCE_칠호선)));
        칠호선.addSection(Section.of(칠호선, 반포, 고속터미널_END, Distance.of(DISTANCE_칠호선)));

        Line 경의중앙선 = lineRepository.save(Line.of("경의중앙선", "민트", 왕십리_START, 응봉, DISTANCE_경의중앙선));
        경의중앙선.addSection(Section.of(경의중앙선, 응봉, 옥수, Distance.of(DISTANCE_경의중앙선)));

        Line 삼호선 = lineRepository.save(Line.of("삼호선", "주황", 옥수, 압구정, DISTANCE_삼호선));
        삼호선.addSection(Section.of(삼호선, 압구정, 신사, Distance.of(DISTANCE_삼호선)));
        삼호선.addSection(Section.of(삼호선, 신사, 잠원, Distance.of(DISTANCE_삼호선)));
        삼호선.addSection(Section.of(삼호선, 잠원, 고속터미널_END, Distance.of(DISTANCE_삼호선)));
    }

    @Test
    @DisplayName("구간을 조회한다.")
    void create() {
        // given
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        JGraphPathFinder pathFinder = new JGraphPathFinder();

        // when
        Path path = pathFinder.getShortestPath(lines, stations, 왕십리_START.getId(), 고속터미널_END.getId());

        // then
        assertThat(path.routes()).hasSize(7);
        assertThat(path.distance()).isEqualTo(Distance.of(52));
    }

    @Test
    @DisplayName("출발지와 목적지를 같게 한다")
    void sameStations() {
        // given
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        JGraphPathFinder pathFinder = new JGraphPathFinder();

        // when
        assertThatThrownBy(() -> pathFinder.getShortestPath(lines, stations, 왕십리_START.getId(), 왕십리_START.getId()))
                .isInstanceOf(PathBeginIsEndException.class)
                .hasMessageContaining(PathBeginIsEndException.message);
    }

    @Test
    @DisplayName("경로가 없는 경우")
    void noPath() {
        // given
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        JGraphPathFinder pathFinder = new JGraphPathFinder();

        // when
        assertThatThrownBy(() -> pathFinder.getShortestPath(lines, stations, 왕십리_START.getId(), 경로없는역.getId()))
                .isInstanceOf(PathNotFoundException.class)
                .hasMessageContaining(PathNotFoundException.message);
    }

    @Test
    @DisplayName("존재하지 않은 역 예외")
    void noStations() {
        // given
        Long 존재하지않은역 = Long.MAX_VALUE;
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        JGraphPathFinder pathFinder = new JGraphPathFinder();

        // when
        assertThatThrownBy(() -> pathFinder.getShortestPath(lines, stations, 왕십리_START.getId(), 존재하지않은역))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining(StationNotFoundException.message);
    }

    @Test
    @DisplayName("경로 찾기를 이용해서 즐겨찾기를 생성한다")
    void transFavorite() {
        // given
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        JGraphPathFinder pathFinder = new JGraphPathFinder();

        // when
        Path path = pathFinder.getShortestPath(lines, stations, 왕십리_START.getId(), 고속터미널_END.getId());
        사용자.addFavorite(Favorite.of(path));

        Member findMember = memberRepository.findById(사용자.getId()).orElseThrow(MemberNotFoundException::new);

        // then
        assertThat(findMember.getFavorites()).hasSize(1);
        assertThat(findMember.getFavorites().get(0).getDistance()).isEqualTo(path.distance());
    }
}