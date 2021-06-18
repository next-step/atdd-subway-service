package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.ApproveException;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class FavoriteQueryService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;


    public FavoriteQueryService(MemberRepository memberRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findAllByMember(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember.getEmail());

        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(AuthorizationException::new);
    }
}
