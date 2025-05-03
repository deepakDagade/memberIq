package com.nexoraa.memberiq.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {

	private int status = 200;
	private String statusMessage;
	private List<Object> data = new ArrayList<>();

	public Response(int status, String statusMessage, List<Object> data) {
		this.status = status;
		this.statusMessage = statusMessage;
		this.data = data != null ? data : new ArrayList<>();
	}

	public boolean addData(Object object) {
		return data.add(object);
	}

	public Object getData() {
		if (Objects.isNull(this.data) || this.data.isEmpty()) {
			return new ArrayList<>();
		}
		return this.data.getFirst();
	}

	@Override
	public String toString() {
		return "{\n" + "  status : " + status + ",\n" + "  statusMessage : " + statusMessage + ",\n" + "  data : "
				+ data + "\n" + "}";
	}

}