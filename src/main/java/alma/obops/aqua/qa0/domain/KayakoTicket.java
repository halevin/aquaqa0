package alma.obops.aqua.qa0.domain;

public class KayakoTicket {
	
	private int id;
	private String email;
	private String subject;
	private String status;
	private String url;
//	private Date dueDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public Date getDueDate() {
//		return dueDate;
//	}

//	public void setDueDate(Date dueDate) {
//		this.dueDate = dueDate;
//	}

	@Override
	public String toString() {
		return "KayakoTicket[id=" + id + 
				", email=" + email + 
				", status=" + status +
				", subject=" + subject + 
				", url=" + url /*+ ", dueDate=" + dueDate*/ + "]";
	}
}
