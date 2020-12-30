package nextstep.subway.favorite.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponse {
	private Long id;
	private MemberResponse member;
	private StationResponse source;
	private StationResponse target;

	private FavoriteResponse(Long id, MemberResponse member, StationResponse source, StationResponse target) {
		this.id = id;
		this.member = member;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {

		return new FavoriteResponse(
			favorite.getId(),
			MemberResponse.of(favorite.getMember()),
			StationResponse.of(favorite.getSource()),
			StationResponse.of(favorite.getTarget())
		);
	}
}
