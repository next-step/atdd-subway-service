package nextstep.subway.favorite.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class FavoriteRequest {
	@NotNull
	private final Long source;
	@NotNull
	private final Long target;
}
