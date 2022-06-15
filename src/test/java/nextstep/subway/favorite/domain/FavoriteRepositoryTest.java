package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FavoriteRepositoryTest {
    private Station 강남역;
    private Station 양재역;
    private Member 회원;

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        회원 = memberRepository.save(new Member("email@email.com", "password", 20));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
    }

    @Test
    void 즐겨찾기_생성() {
        Favorite favorite = favoriteRepository.save(new Favorite.Builder()
                .member(회원)
                .source(강남역)
                .target(양재역)
                .build());

        assertThat(favorite).isNotNull();
    }
}