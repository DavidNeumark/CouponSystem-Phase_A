package d.CouponSystemSingleton;

import java.sql.SQLException;

import b.Enum.ClientType;
import core.exceptions.CompanyNotFound;
import core.exceptions.CouponSystemException;
import core.exceptions.CustomerNotFound;
import d.Facade.AdminFacade;
import d.Facade.CompanyFacade;
import d.Facade.CouponClientFacade;
import d.Facade.CustomerFacade;
import d.Thread.DailyCouponExpirationTask;

public class CouponSystem {

	private static CouponSystem instance;
	private DailyCouponExpirationTask dailyCouponExpirationTask;
	Thread dailyThread;

	private CouponSystem() {

		dailyCouponExpirationTask = new DailyCouponExpirationTask();
		dailyThread = new Thread(dailyCouponExpirationTask);
		dailyThread.start();

	}

	public static synchronized CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws SQLException, CouponSystemException, CompanyNotFound, CustomerNotFound {
		switch (clientType) {

		case ADMIN:
			AdminFacade adminFacade = new AdminFacade();
			return adminFacade.login(name, password, clientType);

		case COMPANY:
			CompanyFacade companyFacade = new CompanyFacade();
			return companyFacade.login(name, password, clientType);

		case CUSTOMER:
			CustomerFacade customerFacade = new CustomerFacade();
			return customerFacade.login(name, password, clientType);

		}
		return null;
	}

	public void shutdown() {

		// TODO turn off the daily task
		dailyCouponExpirationTask.stopTask();
		dailyThread.interrupt();
		// ConnectionPool couponConnPool = ConnectionPool.getInstance();
		// couponConnPool.closeAllConnections();
	}

}
