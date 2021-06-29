package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteQueryServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Member savedMember;

    private Station savedStation1;
    private Station savedStation2;
    private Station savedStation3;

    private Line savedLine;

    private FavoriteQueryService favoriteQueryService;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(new Member("EMAIL@EMAIL.com", "PASSWORD", 25));

        savedStation1 = stationRepository.save(new Station("STATION_1"));
        savedStation2 = stationRepository.save(new Station("STATION_2"));
        savedStation3 = stationRepository.save(new Station("STATION_3"));

        savedLine = lineRepository.save(new Line("LINE", "LINE", 0, savedStation1, savedStation2, 10));

        favoriteQueryService = new FavoriteQueryService(favoriteRepository);
    }

    @Test
    @DisplayName("등록된 계정이 조회하려 하면 조회가 성공한다")
    void 등록된_계정이_조회하려_하면_조회가_성공한다() {
        // given
        Member newMember = memberRepository.save(new Member("NEWNEW@EMAIL.com", "NEWNEW", 11));
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());
        Lines lines = new Lines(Arrays.asList(savedLine));

        savedLine.addSection(new Section(savedStation2, savedStation3, new Distance(10)));

        // when
        Favorite favorite1 = favoriteRepository.save(Favorite.create(lines, savedMember, savedStation1, savedStation2));
        Favorite favorite2 = favoriteRepository.save(Favorite.create(lines, savedMember, savedStation2, savedStation3));
        Favorite favorite3 = favoriteRepository.save(Favorite.create(lines, savedMember, savedStation1, savedStation3));

        favoriteRepository.save(Favorite.create(lines, newMember, savedStation1, savedStation3));

        List<FavoriteResponse> allFavorites = favoriteQueryService.findAllByMember(loginMember);

        // then
        assertThat(allFavorites).hasSize(3);
        assertThat(allFavorites)
                .containsExactlyInAnyOrder(
                        FavoriteResponse.of(favorite1),
                        FavoriteResponse.of(favorite2),
                        FavoriteResponse.of(favorite3)
                );
    }
}