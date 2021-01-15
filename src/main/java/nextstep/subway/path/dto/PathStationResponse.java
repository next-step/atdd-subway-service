package nextstep.subway.path.dto;

import java.time.LocalDateTime;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class PathStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	public PathStationResponse(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
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
