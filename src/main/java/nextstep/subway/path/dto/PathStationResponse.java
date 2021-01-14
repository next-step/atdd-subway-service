package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.List;

import nextstep.subway.path.domain.SubwayPathStation;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
public class PathStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdAt;


	public PathStationResponse(SubwayPathStation subwayPathStation) {
		this.id = subwayPathStation.getId();
		this.name = subwayPathStation.getName();
		this.createdAt = subwayPathStation.getCreatedAt();
	}

	protected PathStationResponse() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
