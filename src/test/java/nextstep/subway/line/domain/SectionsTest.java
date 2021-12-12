package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.infrastructure.JGraphPathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SectionsTest
 * author : haedoang
 * date : 2021-11-30
 * description :
 */
@DataJpaTest
public class SectionsTest {
    @Autowired
    private MemberRepository members;

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    SubwayFare subwayFare = new SeoulMetroFare();
    PathFinder pathFinder = new JGraphPathFinder();

    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 마포역 = new Station("마포역");

    private final int 거리_5 = 5;
    private final int 거리_1 = 1;

    private final int 추가요금_1000 = 1_000;
    private final int 추가요금_1500 = 1_500;

    @Test
    @DisplayName("구간 추가 검증")
    public void addSection() {
        // given
        Line line = Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5);

        // when
        line.addSection(Section.of(line, 강남역, 마포역, Distance.of(1)));

        // then
        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getStations()).hasSize(3);
        assertThat(line.getStations()).containsExactly(강남역, 마포역, 광교역);
    }

    @Test
    @DisplayName("구간 삭제 검증")
    public void removeStation() {
        // given
        stations.saveAll(Arrays.asList(강남역, 마포역, 광교역));
        Line line = lines.save(Line.of("신분당선", "빨강", 강남역, 광교역, 거리_5));
        line.addSection(Section.of(line, 마포역, 광교역, Distance.of(거리_1)));

        // when
        line.removeStation(마포역.getId());

        // then
        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations()).containsExactly(강남역, 광교역);
    }

    @Test
    @DisplayName("구간의 최대 추가요금과 사용자별 요금 구하기")
    public void maxExtraCharge() throws Exception {
        //given
        LoginMember infantMember = LoginMember.of(members.save(new Member("haedoang@gmail.com", "11", 3)));
        LoginMember childMember = LoginMember.of(members.save(new Member("misster@gmail.com", "11", 10)));
        LoginMember youthMember = LoginMember.of(members.save(new Member("koko1945@gmail.com", "11", 19)));
        LoginMember adultMember = LoginMember.of(members.save(new Member("cometholic@gmail.com", "11", 33)));


        stations.saveAll(Arrays.asList(강남역, 마포역, 광교역));
        lines.save(Line.of("신분당선", "빨강", 강남역, 마포역, 거리_5, 추가요금_1500));
        lines.save(Line.of("2호선", "녹색", 마포역, 광교역, 거리_5, 추가요금_1000));

        //when
        final Path shortestPath = pathFinder.getShortestPath(lines.findLines(), stations.findAll(), 강남역.getId(), 광교역.getId());

        //then
        assertThat(subwayFare.rateInquiry(shortestPath, infantMember)).isEqualTo(Money.of(0));
        assertThat(subwayFare.rateInquiry(shortestPath, childMember)).isEqualTo(Money.of(1_950));
        assertThat(subwayFare.rateInquiry(shortestPath, youthMember)).isEqualTo(Money.of(2_220));
        assertThat(subwayFare.rateInquiry(shortestPath, adultMember)).isEqualTo(Money.of(2_750));
    }
}
