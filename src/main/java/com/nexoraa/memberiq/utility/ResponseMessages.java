package com.nexoraa.memberiq.utility;

public class ResponseMessages {
	public static final String USER_ADDED_SUCCESS = "User added successfully!";
	public static final String USER_FOUND = "Users found!";
	public static final String USER_NOT_FOUND = "User not found!";
	public static final String USER_DATA_FOUND = "Users found!";
	public static final String USER_DATA_NOT_FOUND = "User not found!";
	public static final String USER_UPDATED_SUCCESS = "User status updated successfully!";
	public static final String USER_DELETE_SUCCESS = "Users deleted successfully!";
	public static final String INVALID_REQUEST = "Invalid request.";
	public static final String INTERNAL_SERVER_ERROR = "Internal server error!";
	public static final String ORGANIZATION_NOT_FOUND = "Organization not found!";
	public static final String EMAIL_ALREADY_EXIST = "User with email already exists!";
	public static final String EMAIL_NOT_VERIFIED = "Email not verified!";
	public static final String PAGEABLE_CANNOT_BE_NULL = "Pagable not found!";
	public static final String INVALID_STATUS_VALUE = "Invalid status value!";

	// Profile response message
	public static final String PROFILES_NOT_FOUND = "Profiles not found!";
	public static final String PHONE_NUMBER_ALREADY_REGISTERED = "Phone number '%s' already exist!";
	public static final String PROFILE_ADDED_SUCCESS = "Profile added sucessfully!";
	public static final String PROFILES_FOUND = "profile data found!";
	public static final String PROFILE_UPDATED_SUCCESS = "Profile update sucessfully!";
	public static final String PROFILE_NOT_FOUND = "Profile not found!";
	public static final String PROFILES_DATA_NOT_FOUND = "Profile data not found!";
	public static final String INVALID_PROFILE_TYPE_VALUE = "Invalid profile type value!";
	
	// Group response message
	public static final String GROUP_CREATED_SUCCESS = "Group created successfully!";
	public static final String GROUP_UPDATED_SUCCESS = "Group updated successfully!";
	public static final String GROUP_DELETED_SUCCESS = "Group deleted successfully!";
	public static final String GROUP_FETCH_SUCCESS = "Group fetched successfully!";
	public static final String GROUPS_FETCH_SUCCESS = "Groups fetched successfully!";
	public static final String GROUP_NOT_FOUND = "Group not found!";
	public static final String GROUP_NAME_ALREADY_EXIST = "Group name '%s' Already exist!";
	public static final String GROUP_DATA_NOT_FOUND = "Group data not found!";
	public static final String GROUPS_DATA_FOUND = "Group data found!";

	// password change reset message
	public static final String PASSWORD_RESET_EMAIL_SENT_SUCCESS = "Password reset email send success!";
	public static final String PASSWORD_RESET_SUCCESS = "Password reset sucessfully!";
	public static final String ERROR_INVALID_TOKEN = "Invalid password reset token!";
	public static final String TOKEN_EXPIRED = "Password token expired!";

	// organization message
	public static final String ORGANIZATION_ADDED_SUCCESS = "Organization added sucessfully!";
	public static final String ORGANIZATION_UPDATED_SUCCESS = "Organization update sucessfully!";
	public static final String ORGANIZATION_FOUND = "Organization data found!";
	public static final String ORGANIZATION_CONFIG_UPDATED_SUCCESS = "Organization config updat sucessfully!";
	public static final String LOGO_UPLOAD_SUCESS = "Organization logo updated sucessfully!";
	public static final String ORGANIZATION_ALREADY_EXIST = "Organization already exist with same name!";

	// PayMent mode messages
	public static final String PAYMENT_MODE_CREATED_SUCCESS = "Payment mode created sucessfully!";
	public static final String PAYMENT_MODES_DATA_FOUND = "Payment mode Data found!";
	public static final String PAYMENT_MODES_DATA_NOT_FOUND = "Payment mode Data not found!";
	public static final String PAYMENT_MODE_UPDATED_SUCCESS = "Payment mode updated sucessfully!";
	public static final String PAYMENT_MODES_FETCH_SUCCESS = "Payment mode data fetch sucessfully!";
	public static final String PAYMENT_MODE_NAME_ALREADY_EXISTS = "Payment mode name already exist!";

	// Membership type
	public static final String MEMBERSHIP_TYPE_CREATED_SUCCESS = "Membership type created successfully.";
	public static final String MEMBERSHIP_TYPE_UPDATED_SUCCESS = "Membership type updated successfully.";

	public static final String MEMBERSHIP_TYPE_NAME_ALREADY_EXISTS = "A membership type with the name '%s' already exists.";
	public static final String MEMBERSHIP_TYPE_NOT_FOUND = "Membership type not found.";
	public static final String MEMBERSHIP_TYPES_FETCH_SUCCESS = "Membership types fetched successfully.";
	public static final String NO_MEMBERSHIP_TYPES_AVAILABLE = "No membership types available.";

	// Messages for PaymentDetails operations
	public static final String PAYMENT_DETAIL_CREATED_SUCCESS = "Payment detail created successfully.";
	public static final String PAYMENT_DETAIL_UPDATED_SUCCESS = "Payment detail updated successfully.";
	public static final String PAYMENT_DETAIL_STATUS_UPDATED_SUCCESS = "Payment detail status updated successfully.";
	public static final String PAYMENT_DETAILS_DATA_NOT_FOUND = "No payment details found.";
	public static final String PAYMENT_DETAILS_FETCH_SUCCESS = "Payment details fetched successfully.";
	public static final String PAYMENT_DETAILS_DATA_FOUND = "Payment details data found.";
	public static final String PAYMENT_DETAIL_NOT_FOUND = "Payment detail not found.";

	// You can also add messages for error cases, for example
	public static final String PAYMENT_DETAIL_ALREADY_EXISTS = "Payment detail with this information already exists.";
	public static final String PAYMENT_DETAIL_BULK_UPDATE_FAILED = "Bulk update of payment details failed.";

	// Messages for FrontOffice operations
	public static final String FRONT_OFFICE_CREATED_SUCCESS = "Front office entry created successfully.";
	public static final String FRONT_OFFICE_UPDATED_SUCCESS = "Front office entry updated successfully.";
	public static final String FRONT_OFFICES_DATA_NOT_FOUND = "No front office entries found.";
	public static final String FRONT_OFFICES_DATA_FOUND = "Front office data found.";
	public static final String FRONT_OFFICES_FETCH_SUCCESS = "Front offices have been fetched successfully.";
	public static final String FRONT_OFFICE_NOT_FOUND = "Front office entry not found.";
	public static final String INVALID_DATE_RANGE_FORMAT = "Invalid date range format!";

	// ExpenseCategory Messages
	public static final String EXPENSE_CATEGORY_CREATED_SUCCESS = "Expense category created successfully.";
	public static final String EXPENSE_CATEGORY_UPDATED_SUCCESS = "Expense category updated successfully.";
	public static final String EXPENSE_CATEGORY_NOT_FOUND = "Expense category not found.";
	public static final String EXPENSE_CATEGORY_NAME_ALREADY_EXISTS = "Expense category with name '%s' already exists.";
	public static final String EXPENSE_CATEGORIES_DATA_NOT_FOUND = "Expense categories not found.";
	public static final String EXPENSE_CATEGORIES_DATA_FOUND = "Expense categories data found.";
	public static final String EXPENSE_CATEGORIES_FETCH_SUCCESS = "Expense categories fetched successfully.";

	// Messages for Expense operations
	public static final String EXPENSE_CREATED_SUCCESS = "Expense created successfully.";
	public static final String EXPENSE_UPDATED_SUCCESS = "Expense updated successfully.";
	public static final String EXPENSE_NOT_FOUND = "Expense not found.";
	public static final String EXPENSES_DATA_FOUND = "Expenses data found.";
	public static final String EXPENSE_DATA_NOT_FOUND = "No expenses found.";
	public static final String EXPENSES_FETCH_SUCCESS = "Expenses fetched successfully.";

	// Membership messages
	public static final String MEMBERSHIP_CREATED_SUCCESS = "Membership created successfully.";
	public static final String MEMBERSHIP_UPDATED_SUCCESS = "Membership updated successfully.";
	public static final String MEMBERSHIP_NOT_FOUND = "Membership not found.";
	public static final String MEMBERSHIPS_DATA_FOUND = "Memberships data found.";
	public static final String MEMBERSHIP_DATA_NOT_FOUND = "No memberships available.";
	public static final String MEMBERSHIPS_FETCH_SUCCESS = "Memberships fetched successfully.";
	public static final String MEMBERSHIP_ALREADY_EXISTS = "A membership with the same details already exists.";
	public static final String MEMBERSHIP_DELETED_SUCCESS = "Membership deleted successfully.";
	public static final String MEMBERSHIP_BULK_UPDATE_SUCCESS = "Memberships status updated successfully.";
	public static final String INVALID_MEMBERSHIP_DATA = "Provided membership data is invalid.";
	
	//Dashboard data
	public static final String DASHBOARD_DATA_FOUND = "DashBoard data found!";
	public static final String DASHBOARD_DATA_NOT_FOUND = "DashBoard data found!";
	

}
