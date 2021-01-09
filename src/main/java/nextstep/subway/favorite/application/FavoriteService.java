package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
	}

	public List<FavoriteResponse> getFavorites(LoginMember loginMember) {

	}

	public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {

	}

	public void deleteFavoriteById(LoginMember loginMember, Long favoriteId) {

	}
}
