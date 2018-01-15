package enumPackage;

public enum CouponType {

	RESTAURANTS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING;

	public static CouponType convertToCouponType(String type) {

		CouponType couponType = null;

		switch (type) {
		case "RESTAURANTS":
		case "Restaurants":
		case "restaurants":
			couponType = CouponType.RESTAURANTS;
			break;
		case "ELECTRICITY":
		case "Electricity":
		case "electricity":
			couponType = CouponType.ELECTRICITY;
			break;
		case "FOOD":
		case "Food":
		case "food":
			couponType = CouponType.FOOD;
			break;
		case "HEALTH":
		case "Health":
		case "health":
			couponType = CouponType.HEALTH;
			break;
		case "SPORTS":
		case "Sports":
		case "sports":
			couponType = CouponType.SPORTS;
			break;
		case "CAMPING":
		case "Camping":
		case "camping":
			couponType = CouponType.CAMPING;
			break;
		case "TRAVELLING":
		case "Travelling":
		case "travelling":
			couponType = CouponType.TRAVELLING;
			break;
		}

		return couponType;
	}

	public static String convertToString(CouponType type) {

		String couponType = null;

		switch (type) {
		case RESTAURANTS:
			couponType = "RESTAURANTS";
			break;
		case ELECTRICITY:
			couponType = "ELECTRICITY";
			break;
		case FOOD:
			couponType = "FOOD";
			break;
		case HEALTH:
			couponType = "HEALTH";
			break;
		case SPORTS:
			couponType = "SPORTS";
			break;
		case CAMPING:
			couponType = "CAMPING";
			break;
		case TRAVELLING:
			couponType = "TRAVELLING";
			break;
		}
		return couponType;
	}

}
