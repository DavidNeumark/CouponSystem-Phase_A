package beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ID;
	private String custName;
	private String custEmail;
	private String password;
	private Set<Coupon> coupons = new HashSet<>();

	/**
	 * Class CTOR
	 */
	public Customer() {
		super();
	}

	/**
	 * Class CTOR with parameters
	 * 
	 * @param iD
	 * @param custName
	 * @param custEmail
	 * @param password
	 * @param coupons
	 */
	public Customer(long iD, String custName, String custEmail, String password, Set<Coupon> coupons) {
		super();
		ID = iD;
		this.custName = custName;
		this.custEmail = custEmail;
		this.password = password;
		this.coupons = coupons;
	}

	/**
	 * Class CTOR with parameters
	 * 
	 * @param custName
	 * @param custEmail
	 * @param password
	 * @param coupons
	 */
	public Customer(String custName, String custEmail, String password, Set<Coupon> coupons) {
		super();
		this.custName = custName;
		this.custEmail = custEmail;
		this.password = password;
		this.coupons = coupons;
	}

	/**
	 * Class CTOR with parameters
	 * 
	 * @param iD
	 * @param custName
	 * @param custEmail
	 * @param password
	 */
	public Customer(long iD, String custName, String custEmail, String password) {
		super();
		ID = iD;
		this.custName = custName;
		this.custEmail = custEmail;
		this.password = password;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [ID=" + ID + ", custName=" + custName + ", custEmail=" + custEmail + ", password=" + password
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((coupons == null) ? 0 : coupons.hashCode());
		result = prime * result + ((custEmail == null) ? 0 : custEmail.hashCode());
		result = prime * result + ((custName == null) ? 0 : custName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (ID != other.ID)
			return false;
		if (coupons == null) {
			if (other.coupons != null)
				return false;
		} else if (!coupons.equals(other.coupons))
			return false;
		if (custEmail == null) {
			if (other.custEmail != null)
				return false;
		} else if (!custEmail.equals(other.custEmail))
			return false;
		if (custName == null) {
			if (other.custName != null)
				return false;
		} else if (!custName.equals(other.custName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

}
