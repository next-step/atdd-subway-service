package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.ApproveException;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.EntityNotExistException;
import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class FavoriteCommandServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Member savedMember = new Member("EMAIL@EMAIL.com", "PASSWORD", 25);
    private LoginMember savedLoginMember = null;

    private Station savedStation1 = new Station("STATION_1");
    private Station savedStation2 = new Station("STATION_2");
    private Station savedStation3 = new Station("STATION_3");

    private Line savedLine = new Line("LINE", "LINE", 0, savedStation1, savedStation2, 10);

    private FavoriteCommandService favoriteCommandService;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(savedMember);
        savedLoginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());

        savedStation1 = stationRepository.save(savedStation1);
        savedStation2 = stationRepository.save(savedStation2);
        savedStation3 = stationRepository.save(savedStation3);

        savedLine = lineRepository.save(savedLine);

        favoriteCommandService = new FavoriteCommandService(stationRepository, lineRepository, memberRepository, favoriteRepository);
    }

    @Test
    @DisplayName("등록되지 않은 회원이 등록하려 하면 AuthorizationException이 발생한다")
    void 등록되지_않은_회원이_등록하려_하면_AuthorizationException이_발생한다() {
        assertThatExceptionOfType(AuthorizationException.class)
                .isThrownBy(() -> favoriteCommandService.createFavorite(new LoginMember(1L, "NONE@MAIL.com", 25), new FavoriteRequest(savedStation1.getId(), savedStation2.getId())));
    }

    @Test
    @DisplayName("연결되지 않은 역을 등록하려 하면 LineHasNotExistStationException이 발생한다")
    void 연결되지_않은_역을_등록하려_하면_LineHasNotExistStationException이_발생한다() {
        assertThatExceptionOfType(LineHasNotExistStationException.class)
                .isThrownBy(() -> favoriteCommandService.createFavorite(savedLoginMember, new FavoriteRequest(savedStation1.getId(), savedStation3.getId())));

        assertThatExceptionOfType(LineHasNotExistStationException.class)
                .isThrownBy(() -> favoriteCommandService.createFavorite(
                        savedLoginMember,
                        new FavoriteRequest(savedStation3.getId(), savedStation1.getId()))
                );
    }

    @Test
    @DisplayName("등록된 계정이며, 연결된 역을 등록하려 하면 성공한다")
    void 등록된_계정이며_연결된_역을_등록하려_하면_성공한다() {
        FavoriteResponse favoriteResponse = assertDoesNotThrow(() -> favoriteCommandService.createFavorite(
                savedLoginMember,
                new FavoriteRequest(savedStation1.getId(), savedStation2.getId()))
        );

        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.of(savedStation1));
        assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.of(savedStation2));
    }

    @Test
    @DisplayName("본인의 즐겨찾기가 아닌것을 삭제하려 하면 NotOwnerException이 발생한다")
    void 본인의_즐겨찾기가_아닌것을_삭제하려_하면_ApproveException이_발생한다() {
        // given
        Member newMember = memberRepository.save(new Member("NEWNEW@EMAIL.com", "NEWNEW", 11));
        LoginMember newLoginMember = new LoginMember(newMember.getId(), newMember.getEmail(), newMember.getAge());

        FavoriteResponse favorite = favoriteCommandService.createFavorite(savedLoginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        // when & then
        assertThatExceptionOfType(ApproveException.class)
                .isThrownBy(() -> favoriteCommandService.deleteById(newLoginMember, favorite.getId()));
    }

    @Test
    @DisplayName("본인의 즐겨찾기를 삭제하려면 성공한다")
    void 본인의_즐겨찾기를_삭제하려면_성공한다() {
        FavoriteResponse favorite = favoriteCommandService.createFavorite(savedLoginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        assertDoesNotThrow(() -> favoriteCommandService.deleteById(savedLoginMember, favorite.getId()));
    }

    @Test
    @DisplayName("등록되지 않은 즐겨찾기를 삭제하려 하면 EntityNotExistException 발생한다")
    void 등록되지_않은_즐겨찾기를_삭제하려_하면_EntityNotExistException이_발생한다() {
        // given
        FavoriteResponse favorite = favoriteCommandService.createFavorite(savedLoginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        // when
        favoriteCommandService.deleteById(savedLoginMember, favorite.getId());

        // then
        assertThatExceptionOfType(EntityNotExistException.class)
                .isThrownBy(() -> favoriteCommandService.deleteById(savedLoginMember, favorite.getId()));
    }
}