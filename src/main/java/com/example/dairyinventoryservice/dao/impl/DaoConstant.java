package com.example.dairyinventoryservice.dao.impl;

public class DaoConstant {

    public final static String GET_USER_AUTH_BY_EMAIL = "{CALL get_user_auth_by_email(?)}";

    public final static String INSERT_PURCHASE = "{CALL insert_purchase_details( ?, ?, ?, ?, ?, ?)}";

    public final static String INSERT_USER = "{CALL insert_user_details( ?, ?, ?, ?, ?, ?, ?)}";

    public final static String UPDATE_USER = "{CALL update_user_details( ?, ?, ?, ?, ?, ?, ?)}";

    public final static String CHANGE_PASSWORD = "{CALL change_password( ?, ?, ?)}";

    public final static String INSERT_USER_ROLE = "{CALL insert_user_role(?)}";

    public final static String INSERT_LOCATION = "{CALL insert_location_details(?)}";

    public final static String INSERT_ITEM_DETAIL =  "{CALL insert_item_details( ?, ?, ?, ?, ?)}";

    public final static String INSERT_SALES_DETAILS =  "{CALL insert_sales_details( ?, ?, ?, ?, ?, ?)}";

    public final static String GET_USER_DETAIL_BY_EMAIL_ID = "{CALL get_user_details_by_email_id(?)}";

    public final static String GET_PURCHASE_DETAILS_BY_DATE_RANGE = "{CALL get_purchase_details_by_date_range( ?, ?)}";

    public final static String GET_ITEM_DETAILS = "{CALL get_item_details()}";

    public final static String GET_LOCATION_DETAILS = "{CALL get_location_details()}";

    public final static String GET_GOING_TO_EXPIRY_PRODUCT_AND_LOCATION = "{CALL get_going_to_expiry_product_and_location()}";

    public final static String GET_NO_OF_AVAILABLE_ITEMS = "{CALL get_no_of_available_item( ?, ?)}";

    public final static String GET_NO_OF_PURCHASE_DETAILS_BY_LOCATION = "{CALL get_purchase_details_by_date_range_and_location( ?, ?)}";

    public final static String GET_REVENUE_BY_DATE = "{CALL get_revenue_details_by_date_range( ?, ?)}";

    public final static String GET_REVENUE_BY_DATE_AND_LOCATION = "{CALL get_revenue_details_by_date_range_and_location( ?, ?, ?)}";

}