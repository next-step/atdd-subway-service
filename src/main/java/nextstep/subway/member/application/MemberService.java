package nextstep.subway.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.favorites.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberById(id));
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

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public void addFavorite(Long id, FavoriteRequest request) {
        Member member = findMemberById(id);
        Path path = pathService.getShortestPath(request.getSourceStationId(), request.getTargetStationId());
        member.addFavorite(Favorite.of(path));
    }
}
