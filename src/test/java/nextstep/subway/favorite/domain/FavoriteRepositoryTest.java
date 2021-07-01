package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    private Member member;
    private Station station1;
    private Station station2;
    private Line line;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE));
        station1 = new Station("강남역");
        station2 = new Station("교대역");
        stationRepository.saveAll(Arrays.asList(station1, station2));
        line = lineRepository.save(new Line("2호선", "green", station1, station2, 3));
    }

    @Test
    @DisplayName("즐겨찾기 저장")
    void save() {
        // when
        Favorite favorite = favoriteRepository.save(new Favorite(member, station1, station2));

        // then
        assertThat(favorite.getMember()).isSameAs(member);
    }

    @Test
    @DisplayName("사용자 ID로 즐겨찾기 조회")
    void findAll_by_memberId() {
        // given
        favoriteRepository.save(new Favorite(member, station1, station2));

        // when
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

        // when
        assertThat(favorites.size()).isNotZero();
    }
}
