package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.ApproveException;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class FavoriteServiceTest {
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

    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(savedMember);

        savedStation1 = stationRepository.save(savedStation1);
        savedStation2 = stationRepository.save(savedStation2);
        savedStation3 = stationRepository.save(savedStation3);

        savedLine = lineRepository.save(savedLine);

        favoriteService = new FavoriteService(stationRepository, lineRepository, memberRepository, favoriteRepository);
    }

    @Test
    @DisplayName("등록되지 않은 회원이 등록하려 하면 AuthorizationException이 발생한다")
    void 등록되지_않은_회원이_등록하려_하면_AuthorizationException이_발생한다() {
        assertThatExceptionOfType(AuthorizationException.class)
                .isThrownBy(() -> favoriteService.createFavorite(new LoginMember(1L, "NONE@MAIL.com", 25), new FavoriteRequest(1L, 2L)));
    }

    @Test
    @DisplayName("연결되지 않은 역을 등록하려 하면 LineHasNotExistStationException이 발생한다")
    void 연결되지_않은_역을_등록하려_하면_LineHasNotExistStationException이_발생한다() {
        assertThatExceptionOfType(LineHasNotExistStationException.class)
                .isThrownBy(() -> favoriteService.createFavorite(new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge()), new FavoriteRequest(savedStation1.getId(), savedStation3.getId())));

        assertThatExceptionOfType(LineHasNotExistStationException.class)
                .isThrownBy(() -> favoriteService.createFavorite(
                        new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge()),
                        new FavoriteRequest(savedStation3.getId(), savedStation1.getId()))
                );
    }

    @Test
    @DisplayName("등록된 계정이며, 연결된 역을 등록하려 하면 성공한다")
    void 등록된_계정이며_연결된_역을_등록하려_하면_성공한다() {
        FavoriteResponse favoriteResponse = assertDoesNotThrow(() -> favoriteService.createFavorite(
                new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge()),
                new FavoriteRequest(savedStation1.getId(), savedStation2.getId()))
        );

        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.of(savedStation1));
        assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.of(savedStation2));
    }

    @Test
    @DisplayName("등록되지 않은 회원이 목록을 조회시 AuthorizationException이 발생한다")
    void 등록되지_않은_회원이_목록을_조회시_AuthorizationException이_발생한다() {
        assertThatExceptionOfType(AuthorizationException.class)
                .isThrownBy(() -> favoriteService.findAllByMember(new LoginMember(1L, "NONE@MAIL.com", 25)));
    }

    @Test
    @DisplayName("등록된 계정이 조회하려 하면 조회가 성공한다")
    void 등록된_계정이_조회하려_하면_조회가_성공한다() {
        Member newMember = memberRepository.save(new Member("NEWNEW@EMAIL.com", "NEWNEW", 11));
        LoginMember newLoginMember = new LoginMember(newMember.getId(), newMember.getEmail(), newMember.getAge());

        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());

        savedLine.addSection(new Section(savedStation2, savedStation3, new Distance(10)));

        FavoriteResponse favorite1 = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));
        FavoriteResponse favorite2 = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation2.getId(), savedStation3.getId()));
        FavoriteResponse favorite3 = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation1.getId(), savedStation3.getId()));

        favoriteService.createFavorite(newLoginMember, new FavoriteRequest(savedStation1.getId(), savedStation3.getId()));

        List<FavoriteResponse> allFavorites = favoriteService.findAllByMember(loginMember);

        assertThat(allFavorites).hasSize(3);
        assertThat(allFavorites)
                .containsExactlyInAnyOrder(favorite1, favorite2, favorite3);
    }

    @Test
    @DisplayName("본인의 즐겨찾기가 아닌것을 삭제하려 하면 NotOwnerException이 발생한다")
    void 본인의_즐겨찾기가_아닌것을_삭제하려_하면_ApproveException이_발생한다() {
        Member newMember = memberRepository.save(new Member("NEWNEW@EMAIL.com", "NEWNEW", 11));
        LoginMember newLoginMember = new LoginMember(newMember.getId(), newMember.getEmail(), newMember.getAge());

        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        assertThatExceptionOfType(ApproveException.class)
                .isThrownBy(() -> favoriteService.deleteById(newLoginMember, favorite.getId()));
    }

    @Test
    @DisplayName("본인의 즐겨찾기를 삭제하려면 성공한다")
    void 본인의_즐겨찾기를_삭제하려면_성공한다() {
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        assertDoesNotThrow(() -> favoriteService.deleteById(loginMember, favorite.getId()));
        assertThat(favoriteService.findAllByMember(loginMember)).isEmpty();
    }

    @Test
    @DisplayName("등록되지 않은 즐겨찾기를 삭제하려 하면 EntityNotFoundException이 발생한다")
    void 등록되지_않은_즐겨찾기를_삭제하려_하면_EntityNotFoundException이_발생한다() {
        LoginMember loginMember = new LoginMember(savedMember.getId(), savedMember.getEmail(), savedMember.getAge());
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember, new FavoriteRequest(savedStation1.getId(), savedStation2.getId()));

        favoriteService.deleteById(loginMember, favorite.getId());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> favoriteService.deleteById(loginMember, favorite.getId()));
    }
}