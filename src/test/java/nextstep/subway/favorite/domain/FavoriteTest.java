package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FavoriteTest {
    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.of("lcjltj@gmail.com", "password", 33));
        sourceStation = stationRepository.save(Station.of("강남역"));
        targetStation = stationRepository.save(targetStation = Station.of("서울역"));
    }

    @Test
    @DisplayName("즐겨 찾기 추가")
    void create() {
        // when
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));
        // then
        System.out.println(favorite);
        assertThat(favorite).isInstanceOf(Favorite.class);
    }
}
