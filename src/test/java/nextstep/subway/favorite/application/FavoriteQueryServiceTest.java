package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    private Member savedMember = new Member("EMAIL@EMAIL.com", "PASSWORD", 25);

    private Station savedStation1 = new Station("STATION_1");
    private Station savedStation2 = new Station("STATION_2");
    private Station savedStation3 = new Station("STATION_3");

    private Line savedLine = new Line("LINE", "LINE", savedStation1, savedStation2, 10);

    private FavoriteQueryService favoriteQueryService;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(savedMember);

        savedStation1 = stationRepository.save(savedStation1);
        savedStation2 = stationRepository.save(savedStation2);
        savedStation3 = stationRepository.save(savedStation3);

        savedLine = lineRepository.save(savedLine);

        favoriteQueryService = new FavoriteQueryService(memberRepository, favoriteRepository);
    }


    @Test
    @DisplayName("등록되지 않은 회원이 목록을 조회시 AuthorizationException이 발생한다")
    void 등록되지_않은_회원이_목록을_조회시_AuthorizationException이_발생한다() {
        assertThatExceptionOfType(AuthorizationException.class)
                .isThrownBy(() -> favoriteQueryService.findAllByMember(new LoginMember(1L, "NONE@MAIL.com", 25)));
    }

    @Test
    @DisplayName("등록된 계정이 조회하려 하면 조회가 성공한다")
    void 등록된_계정이_조회하려_하면_조회가_성공한다() {
        Member newMember = memberRepository.save(new Member("NEWNEW@EMAIL.com", "NEWNEW", 11));
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());

        savedLine.addSection(new Section(savedStation2, savedStation3, new Distance(10)));

        Favorite favorite1 = favoriteRepository.save(new Favorite(savedMember, savedStation1, savedStation2));
        Favorite favorite2 = favoriteRepository.save(new Favorite(savedMember, savedStation2, savedStation3));
        Favorite favorite3 = favoriteRepository.save(new Favorite(savedMember, savedStation1, savedStation3));

        favoriteRepository.save(new Favorite(newMember, savedStation1, savedStation3));

        List<FavoriteResponse> allFavorites = favoriteQueryService.findAllByMember(loginMember);

        assertThat(allFavorites).hasSize(3);
        assertThat(allFavorites)
                .containsExactlyInAnyOrder(
                        FavoriteResponse.of(favorite1),
                        FavoriteResponse.of(favorite2),
                        FavoriteResponse.of(favorite3)
                );
    }
}