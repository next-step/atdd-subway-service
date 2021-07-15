package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteResponses;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@Service
public class FavoriteService {

	private FavoriteRepository favoriteRepository;
	private MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
	}

	public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
		return null;
	}

	public FavoriteResponses findAllFavorites(LoginMember loginMember) {
		Member member= memberRepository.findById(loginMember.getId())
			.orElseThrow(()-> new AuthorizationException("멤버를 찾을 수 없습니다."));
		List<Favorite> favorites = favoriteRepository.findAllByMember(member);

		return FavoriteResponses.of(favorites);
	}

	public void deleteFavoriteById(Long id) {
		favoriteRepository.deleteById(id);
	}
}
