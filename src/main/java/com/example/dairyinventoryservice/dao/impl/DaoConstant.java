package com.example.dairyinventoryservice.dao.impl;

public class DaoConstant {
    public final static String INSERT_PURCHASE = "{CALL insert_purchase_details( ?, ?, ?, ?, ?, ?)}";

    public final static String INSERT_USER = "{CALL insert_user( ?, ?, ?, ?, ?)}";

    public final static String UPDATE_USER = "{CALL update_user_details( ?, ?, ?, ?, ?)}";

    public final static String INSERT_USER_ROLE = "{CALL insert_user_role(?)}";

    public final static String INSERT_LOCATION = "{CALL insert_location_details(?)}";

    public final static String INSERT_ITEM_DETAIL =  "{CALL insert_item_details( ?, ?)}";

    public final static String GET_USER_DETAIL_BY_EMAIL_ID = "{CALL get_user_details_by_email_id(?)}";
}
