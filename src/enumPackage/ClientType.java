package enumPackage;

public enum ClientType {

	ADMIN, COMPANY, CUSTOMER;

	public static ClientType convertToClientType(String stringType) {

		ClientType clientType = null;

		switch (stringType) {
		case "ADMIN":
		case "Admin":
		case "admin":
			clientType = ClientType.ADMIN;
			break;
		case "COMPANY":
		case "Company":
		case "company":
			clientType = ClientType.COMPANY;
			break;
		case "CUSTOMER":
		case "Customer":
		case "customer":
			clientType = ClientType.CUSTOMER;
			break;
		}

		return clientType;
	}

}
