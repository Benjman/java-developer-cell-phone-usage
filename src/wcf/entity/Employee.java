package wcf.entity;

import java.util.Date;

public class Employee {
	public final int id;
	public final String name;
	public final Date phonePurchaseDate;
	public final String phoneModel;

	public Employee(final int id,
					final String name,
                    final Date phonePurchaseDate,
                    final String phoneModel) {
		this.id = id;
		this.name = name;
		this.phonePurchaseDate = phonePurchaseDate;
		this.phoneModel = phoneModel;
	}
}
