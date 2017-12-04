package facade;

import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Customer;
import core.exceptions.CouponNotFound;
import core.exceptions.CouponSystemException;
import core.exceptions.DuplicateNameExcetion;
import dbdao.CompanyDBDAO;
import dbdao.CustomerDBDAO;
import enumPackage.ClientType;

/**
 * @author D.Neumark
 */
public class AdminFacade implements CouponClientFacade {

	private CompanyDBDAO companyDBDAO;
	private CustomerDBDAO customerDBDAO;

	/**
	 * AdmninFacade CTOR
	 * 
	 * @throws CouponSystemException
	 */
	public AdminFacade() throws CouponSystemException {
		companyDBDAO = new CompanyDBDAO();
		customerDBDAO = new CustomerDBDAO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see d.Facade.CouponClientFacade#login(java.lang.String, java.lang.String,
	 * b.Enum.ClientType)
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemException {

		if (name.equals("admin") && password.equals("1234") && clientType == ClientType.ADMIN) {
			return this;
		} else {
			throw new CouponSystemException("Login failed!");
		}

	}

	/**
	 * Sets a new company data in the database.
	 * 
	 * @param comp
	 * @return a company object
	 * @throws CouponSystemException
	 */
	public Company createCompany(Company comp) throws CouponSystemException {

		try {
			Company check = companyDBDAO.getCompanyByName(comp.getName());
			if (check == null) {
				companyDBDAO.createCompany(comp);
			} else {
				throw new CouponSystemException("The company: " + comp.getName()
						+ " could not be created, because this name already exists in the database. ");
			}

			// Set<Company> companies = companyDBDAO.getAllCompanies();
			// boolean checker = true;

			// try {
			// if (!companies.isEmpty()) {
			//
			// // checking if the company name already exists in the database. If this name
			// // already exists in the database, the attribute "checker" will receive
			// "false".
			// for (Company company : companies) {
			// if (company.getName().equals(comp.getName())) {
			// checker = false;
			// break;
			// }
			// }
			// }
			// if (checker) {
			// return companyDBDAO.createCompany(comp);
			// }
		} catch (CouponSystemException e) {
			CouponSystemException ex = new CouponSystemException(
					"The company name: " + comp.getName() + " already exist on the data base", e);
			throw ex;
		}
		return null;
	}

	/**
	 * Deletes the company data from the database.
	 * 
	 * @param company
	 *            this the company object that will be deleted
	 * @throws CouponSystemException
	 *             if the company was not deleted, the CouponSystemException will
	 *             appear
	 */
	public void deleteCompany(Company company) throws CouponSystemException {

		companyDBDAO.deleteCompany(company);

	}

	/**
	 * Updates the data of the company in the database. The name of the company will
	 * not be updated.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 *             if the update is not done, then the system will throws the
	 *             exception
	 */
	public void updateCompany(Company company) throws CouponSystemException {

		companyDBDAO.updateCompany(company);

	}

	/**
	 * This method gets the data of the company from the database.
	 * 
	 * @param id
	 * @return Company object
	 * @throws CouponSystemException
	 */
	public Company getCompany(long id) throws CouponSystemException {

		Company company = companyDBDAO.getCompany(id);
		return company;
	}

	/**
	 * This method gets a list with the data of all companies that are in the
	 * database.
	 * 
	 * @return a collection of Company("companies").
	 * @throws CouponSystemException
	 */
	public Set<Company> getAllCompanies() throws CouponSystemException {

		Set<Company> companies = new HashSet<>();
		companies = companyDBDAO.getAllCompanies();
		return companies;
	}

	/**
	 * Gets the company object by the company name
	 * 
	 * @param companyName
	 * @return the company object
	 * @throws CouponSystemException
	 */
	public Company getCompanyByName(String companyName) throws CouponSystemException {
		Company company = new Company();

		company = companyDBDAO.getCompanyByName(companyName);
		if (company != null) {
			return company;
		} else {
			throw new CouponNotFound("This company does not exist.");
		}

	}

	/**
	 * Gets the company id by the company object using the String company name
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void getCompanyIdByName(Company company) throws CouponSystemException {

		companyDBDAO.getCompanyIdByName(company);

	}

	/**
	 * Sets a new customer in the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void createCustomer(Customer customer) throws CouponSystemException {
		try {
			Customer checker = customerDBDAO.getCustomerByName(customer.getCustName());

			if (checker == null) {
				customerDBDAO.createCustomer(customer);
			}

			// Set<Customer> customers = customerDBDAO.getAllCustomers();
			// boolean checker = true;

			// checking if the customer name already exists in the database. If this name
			// already exists in the database, the attribute "checker" will receive "false".
			// if (!customers.isEmpty()) {
			// for (Customer cust : customers) {
			// if (cust.getCustName().equals(customer.getCustName())) {
			// checker = false;
			// break;
			// }
			// }
			// }
			// if (checker) {
			// customerDBDAO.createCustomer(customer);

		} catch (CouponSystemException e) {
			throw new DuplicateNameExcetion("The customer: " + customer.getCustName()
					+ " could not be created, because this name already exists in the database. ");

		}
	}

	/**
	 * Delete the customer data from the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void removeCustomer(Customer customer) throws CouponSystemException {

		customerDBDAO.deleteCustomer(customer);
	}

	/**
	 * Update the customer new data in the database. The name of the company will
	 * not be updated.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {

		customerDBDAO.updateCustomer(customer);

	}

	/**
	 * Gets the customer data from the database.
	 * 
	 * @param id
	 * @return the customer data from the database.
	 * @throws CouponSystemException
	 */
	public Customer getCustomer(long id) throws CouponSystemException {

		return customerDBDAO.getCustomer(id);
	}

	public Customer getCustomerByName(String customerName) throws CouponSystemException {
		Customer customer = new Customer();

		customer = customerDBDAO.getCustomerByName(customerName);
		if (customer != null) {
			return customer;
		} else {
			throw new CouponNotFound("This customer does not exist.");
		}

	}

	/**
	 * This method gets a list with the data of all customers that are in the
	 * database.
	 * 
	 * @return list of customers
	 * @throws CouponSystemException
	 */
	public Set<Customer> getAllCustomers() throws CouponSystemException {

		Set<Customer> customers = new HashSet<>();
		customers = customerDBDAO.getAllCustomers();
		return customers;
	}
}
