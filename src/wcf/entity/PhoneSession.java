package wcf.entity;

import java.util.Date;

public class PhoneSession {
	public final int employeeId;
	public final Date date;
	public final int totalMinutes;
	public final float totalDate;

	public PhoneSession(final int employeeId,
						final Date date,
						final int totalMinutes,
						final float totalDate) {
		this.employeeId = employeeId;
		this.date = date;
		this.totalMinutes = totalMinutes;
		this.totalDate = totalDate;
	}
}
