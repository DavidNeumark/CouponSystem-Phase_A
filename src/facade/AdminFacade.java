package facade;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Customer;
import core.exceptions.CompanyNotFound;
import core.exceptions.CouponNotFound;
import core.exceptions.CouponSystemException;
import core.exceptions.CreateCompanyExcetion;
import core.exceptions.CustomerNotFound;
import core.exceptions.DuplicateNameExcetion;
import core.exceptions.UpdateCompanyException;
import dbdao.CompanyDBDAO;
import dbdao.CustomerDBDAO;
import enumPackage.ClientType;

/**
 * @author David Neumark
 */
public class AdminFacade implements CouponClientFacade {

	private CompanyDBDAO companyDBDAO;
	private CustomerDBDAO customerDBDAO;

	/**
	 * AdmninFacade CTOR
	 */
	public AdminFacade() {
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
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws SQLException, CouponSystemException {

		if (name.equals("admin") && password.equals("1234") && clientType == ClientType.ADMIN) {
			return this;
		} else {
			throw new CouponSystemException("Login failed");
		}

	}

	/**
	 * Sets a new company data in the database.
	 * 
	 * @param comp
	 * @return a company object
	 * @throws CouponSystemException
	 * @throws CreateCompanyExcetion
	 * @throws SQLException
	 */
	public Company createCompany(Company comp) throws CouponSystemException, CreateCompanyExcetion, SQLException {

		Set<Company> companies = companyDBDAO.getAllCompanies();
		boolean checker = true;

		if (!companies.isEmpty()) {

			// checking if the company name already exists in the database. If this name
			// already exists in the database, the attribute "checker" will receive "false".
			for (Company company : companies) {
				if (company.getName().equals(comp.getName())) {
					checker = false;
					break;
				}
			}
		}
		if (checker) {
			return companyDBDAO.createCompany(comp);

		} else {
			System.out.println("Name " + comp.getName() + " already exist on the data base");
			throw new DuplicateNameExcetion("Name " + comp.getName() + " already exist on the data base");
		}
	}

	/**
	 * Deletes the company data from the database.
	 * 
	 * @param Company
	 * @exception SQLException.
	 * @throws CouponSystemException,
	 *             CompanyNameComparator.
	 * @throws SQLException
	 */
	public void deleteCompany(Company company) throws CouponSystemException, SQLException {

		companyDBDAO.deleteCompany(company);

	}

	/**
	 * Updates the data of the company in the database. The name of the company will
	 * not be updated.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 * @throws UpdateCompanyException
	 */
	public void updateCompany(Company company) throws CouponSystemException, UpdateCompanyException {

		companyDBDAO.updateCompany(company);

	}

	/**
	 * This method gets the data of the company from the database.
	 * 
	 * @param id
	 * @return Company object
	 * @throws CouponSystemException
	 * @throws SQLException
	 */
	public Company getCompany(long id) throws CouponSystemException, SQLException {

		Company company = companyDBDAO.getCompany(id);
		return company;
	}

	/**
	 * This method gets a list with the data of all companies that are in the
	 * database.
	 * 
	 * @return a collection of Company("companies").
	 */
	public Set<Company> getAllCompanies() {

		Set<Company> companies = new HashSet<>();
		companies = companyDBDAO.getAllCompanies();
		return companies;
	}

	public Company getCompanyByName(String companyName) throws CompanyNotFound, CouponNotFound {
		Company company = new Company();

		company = companyDBDAO.getCompanyByName(companyName);
		if (company != null) {
			return company;
		} else {
			throw new CouponNotFound("This company does not exist.");
		}

	}

	public void getCompanyIdByName(Company company) {

		companyDBDAO.getCompanyIdByName(company);

	}

	/**
	 * Sets a new customer in the database.
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void createCustomer(Customer customer) throws CouponSystemException {

		Set<Customer> customers = customerDBDAO.getAllCustomers();
		boolean checker = true;

		// checking if the customer name already exists in the database. If this name
		// already exists in the database, the attribute "checker" will receive "false".
		if (!customers.isEmpty()) {
			for (Customer cust : customers) {
				if (cust.getCustName().equals(customer.getCustName())) {
					checker = false;
					break;
				}
			}
		}
		if (checker) {
			customerDBDAO.createCustomer(customer);

		} else {
			System.out.println("Name " + customer.getCustName() + " already exist on the data base");
			throw new DuplicateNameExcetion("Name " + customer.getCustName() + " already exist on the data base");
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

	public Customer getCustomerByName(String customerName) throws CustomerNotFound, CouponNotFound {
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
