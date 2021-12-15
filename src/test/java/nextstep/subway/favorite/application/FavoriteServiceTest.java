package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	@InjectMocks
	private FavoriteService favoriteService;

	@Mock
	private FavoriteRepository favoriteRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private StationRepository stationRepository;

	@DisplayName("즐겨찾기 추가")
	@Test
	void add() {
		final Member member = mockMember(1L);
		final Station 강남역 = mockStation(1L);
		final Station 양재역 = mockStation(2L);

		given(memberRepository.findById(any())).willReturn(Optional.of(member));
		given(stationRepository.findByIdIn(any())).willReturn(Arrays.asList(강남역, 양재역));
		given(favoriteRepository.save(any())).willReturn(Favorite.of(member, 강남역, 양재역));

		final FavoriteResponse favoriteResponse = favoriteService.add(
			member.getId(), new FavoriteRequest(강남역.getId(), 양재역.getId())
		);

		assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역.getId());
		assertThat(favoriteResponse.getTarget().getId()).isEqualTo(양재역.getId());
	}

	@DisplayName("존재하지 않는 회원이 즐겨찾기를 추가하는 경우 예외발생")
	@Test
	void add_not_found_member() {
		final Station 서울역 = mockStation(1L);
		final Station 시청역 = mockStation(2L);
		final Long unknownMemberId = 0L;

		assertThatExceptionOfType(MemberNotFoundException.class)
			.isThrownBy(() -> favoriteService.add(
				unknownMemberId, new FavoriteRequest(서울역.getId(), 시청역.getId())
			));
	}

	@DisplayName("즐겨찾기 목록 조회")
	@Test
	void getAll() {
		final Member member = mockMember(1L);
		final Station 미금역 = mockStation(1L);
		final Station 정자역 = mockStation(2L);
		final Station 서현역 = mockStation(3L);
		final Favorite favorite1 = Favorite.of(member, 미금역, 정자역);
		final Favorite favorite2 = Favorite.of(member, 미금역, 서현역);

		given(favoriteRepository.findAllByMemberId(any())).willReturn(Arrays.asList(favorite1, favorite2));

		final List<FavoriteResponse> favoriteResponse = favoriteService.getAll(member.getId());

		final Set<Long> actualStationIds = favoriteResponse.stream()
			.flatMap(favorite -> Stream.of(favorite.getSource().getId(), favorite.getTarget().getId()))
			.collect(Collectors.toSet());
		assertThat(actualStationIds).containsExactlyInAnyOrder(미금역.getId(), 정자역.getId(), 서현역.getId());
	}

	@DisplayName("자신이 생성하지 않은 즐겨찾기를 삭제하는 경우 예외발생")
	@Test
	void delete_by_other_member() {
		final Member member = mockMember(1L);
		final Member otherMember = mockMember(2L);
		final Station 판교역 = mockStation(1L);
		final Station 정자역 = mockStation(2L);
		final Favorite favorite = Favorite.of(member, 판교역, 정자역);

		given(favoriteRepository.findById(any())).willReturn(Optional.of(favorite));

		assertThatExceptionOfType(AuthorizationException.class)
			.isThrownBy(() -> favoriteService.delete(otherMember.getId(), favorite.getId()));
	}

	private Member mockMember(Long id) {
		final Member member = mock(Member.class);
		given(member.getId()).willReturn(id);
		return member;
	}

	private Station mockStation(Long id) {
		final Station station = mock(Station.class, withSettings().lenient());
		given(station.getId()).willReturn(id);
		return station;
	}
}
