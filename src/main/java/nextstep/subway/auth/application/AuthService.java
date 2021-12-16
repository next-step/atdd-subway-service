package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.Role;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
    this.memberRepository = memberRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public TokenResponse login(TokenRequest request) {
    Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthorizationException("해당 사용자가 존재하지 않습니다."));
    member.checkPassword(request.getPassword());

    String token = jwtTokenProvider.createToken(request.getEmail());
    return new TokenResponse(token);
  }

  public LoginMember findMemberByToken(String credentials) {
    if (!jwtTokenProvider.validateToken(credentials)) {
      return LoginMember.ofGuest();
    }

    String email = jwtTokenProvider.getPayload(credentials);
    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new AuthorizationException("인증 정보가 잘못되었거나 해당 사용자를 찾을 수 없습니다."));
    return new LoginMember(member.getId(), member.getEmail(), member.getAge(), Role.USER);
  }
}
