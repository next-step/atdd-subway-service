package nextstep.subway.auth.dto;

public class LoginRequest {
	private String email;
	private String password;

	public LoginRequest(String id, String password) {
		this.email = id;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
