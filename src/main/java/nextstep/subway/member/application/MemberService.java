package nextstep.subway.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.favorites.dto.FavoriteRequest;
import nextstep.subway.favorites.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final PathService pathService;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public void addFavorite(Long id, FavoriteRequest request) {
        Favorite favorite = getFavorite(request);
        Member member = findMemberById(id);
        member.addFavorite(favorite);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMemberById(memberId);
        member.removeFavorite(favoriteId);
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberById(id));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Favorite getFavorite(FavoriteRequest request) {
        Path path = pathService.getShortestPath(request);
        return Favorite.of(path);
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        Member member = findMemberById(id);
        return FavoriteResponse.ofList(member.getFavorites());
    }
}
