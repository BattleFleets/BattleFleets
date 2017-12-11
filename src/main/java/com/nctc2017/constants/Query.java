package com.nctc2017.constants;

public class Query {
    
    /**
     * This query allows to get any Entity by OBJECT_TYPE_ID and OBJECT_ID.
     * Call execute() or query() must have 4 parameters for PreparedStatement: OBJECT_TYPE_ID, OBJECT_ID, OBJECT_TYPE_ID, OBJECT_ID 
     * */
    public static final String FIND_ANY_ENTITY = 
            "SELECT atr_temp.NAME, atr_val.VALUE"
                    + " FROM OBJECTS entity_obj, ATTRIBUTES atr_temp, OBJECTS entity_templ, ATTRIBUTES_VALUE atr_val"
                    + " WHERE"
                    + "     entity_obj.OBJECT_TYPE_ID = ?"// obj type id
                    + " AND entity_obj.OBJECT_ID = ?"// obj id
                    + " AND entity_templ.OBJECT_ID = entity_obj.SOURCE_ID"

                    + " AND atr_temp.OBJECT_TYPE_ID = entity_templ.OBJECT_TYPE_ID"
                    + " AND atr_val.ATTR_ID = atr_temp.ATTR_ID"
                    + " AND atr_val.OBJECT_ID = entity_templ.OBJECT_ID"

                    + " UNION"
                    + " SELECT atr_obj.NAME, atr_val.VALUE"
                    + " FROM OBJECTS entity_obj, ATTRIBUTES atr_obj, ATTRIBUTES_VALUE atr_val"
                    + " WHERE"
                    + "     entity_obj.OBJECT_TYPE_ID = ?"// obj type id
                    + " AND entity_obj.OBJECT_ID = ?"// obj id"
                    + " AND atr_obj.OBJECT_TYPE_ID = entity_obj.OBJECT_TYPE_ID"

                    + " AND atr_val.ATTR_ID = atr_obj.ATTR_ID"
                    + " AND atr_val.OBJECT_ID = entity_obj.OBJECT_ID";
    
    /**
     * You must have 2 parameters for PreparedStatement: <br>
     * OBJECT_TYPE_ID of container owner<br>
     * OBJECT_ID of owner
     * */
    public static final String FIND_CONTAINER_BY_OWNER_ID = 
            "SELECT container_obj.OBJECT_ID" 
                    + " FROM OBJECTS container_obj" 
                    + " WHERE"
                    + "     container_obj.OBJECT_TYPE_ID = ?"// owner type id
                    + " AND container_obj.PARENT_ID = ?";// id owner obj
    
    /** See this {@link #FIND_CONTAINER_BY_OWNER_ID} */
    public static final String FIND_ALL_IN_CONTAINER_BY_OWNER_ID = 
            "SELECT entities_obj.OBJECT_ID"
                    + " FROM OBJECTS entities_obj" 
                    + " WHERE" 
                    + " entities_obj.PARENT_ID =" 
                    + "    (" + FIND_CONTAINER_BY_OWNER_ID + ")";
   
    /**
     * This query allows to get any Entity by OBJECT_TYPE_ID which locate in any container 
     * like hold, stock, ship by OBJECT_ID of this container.
     * Call execute() or query() must have 4 parameters for PreparedStatement: OBJECT_TYPE_ID, OBJECT_ID of Container, OBJECT_TYPE_ID, OBJECT_ID of Container 
     * */
    public static final String GET_ENTITIES_FROM_CONTAINER = 
            "SELECT entity_obj.OBJECT_ID, atr_temp.NAME, atr_val.VALUE" 
                    + "FROM OBJECTS entity_obj, ATTRIBUTES atr_temp, OBJECTS entity_templ, ATTRIBUTES_VALUE atr_val"
                    + " WHERE"
                    + "    entity_obj.OBJECT_TYPE_ID = ?"//type id entity"
                    + " AND entity_obj.PARENT_ID = ?"// id obj container like hold"
                    + " AND entity_templ.OBJECT_ID = entity_obj.SOURCE_ID"

                    + " AND atr_temp.OBJECT_TYPE_ID = entity_templ.OBJECT_TYPE_ID"
                    + " AND atr_val.ATTR_ID = atr_temp.ATTR_ID"
                    + " AND atr_val.OBJECT_ID = entity_templ.OBJECT_ID"

                    + " UNION"
                    + " SELECT entity_obj.OBJECT_ID, atr_obj.NAME, atr_val.VALUE"
                    + " FROM OBJECTS entity_obj, ATTRIBUTES atr_obj, ATTRIBUTES_VALUE atr_val"
                    + " WHERE"
                    + "    entity_obj.OBJECT_TYPE_ID = ?"// obj type id
                    + " AND entity_obj.PARENT_ID = ?"// id obj container
                    + " AND atr_obj.OBJECT_TYPE_ID = entity_obj.OBJECT_TYPE_ID"

                    + " AND atr_val.ATTR_ID = atr_obj.ATTR_ID"
                    + " AND atr_val.OBJECT_ID = entity_obj.OBJECT_ID";

    public static final String GET_CURRVAL = "SELECT obj_sq.currval FROM DUAL";
    
    /**
     * To get value from field "OBJECT_ID" of "OBJECTS" table
     * This query allows to check is the correct parameter id of some object you have. 
     * You must have 2 parameters for PreparedStatement: OBJECT_ID and OBJECT_TYPE_ID. 
     * */
    public static final String CHECK_OBJECT =
            "SELECT OBJECT_ID"
                    + " FROM OBJECTS"
                    + " WHERE OBJECT_ID = ? AND OBJECT_TYPE_ID = ?";
    
    /**
     * INSERT data to the OBJECTS table.
     * You must have 5 parameters for PreparedStatement:<br> 
     * PARENT_ID(can be null),<br> 
     * OBJECT_TYPE_ID, <br>
     * SOURCE_ID(Template_id),<br>
     * Template_id,<br>
     * ATTR_ID of your template object name (!NOT template OBJECT_TYPE name!)
     * */
    public static final String CREATE_NEW_ENTITY =
            "INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, SOURCE_ID, NAME)"
                    + " VALUES (obj_sq.nextval, ?, ?, ?, "
                    + "(SELECT VALUE FROM ATTRIBUTES_VALUE"
                    + " WHERE OBJECT_ID = ? AND ATTR_ID = ?))";
    
    /**
     * INSERT data to the OBJECTS table.
     * You must have 3 parameters for PreparedStatement:<br> 
     * PARENT_ID(can be null),<br> 
     * OBJECT_TYPE_ID, <br>
     * OBJECT_TYPE_ID.
     * */
    public static final String CRATE_NEW_CONTAINER = 
            "INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, SOURCE_ID, NAME)"
                    + " VALUES (OBJ_SQ.NEXTVAL, ?, ?, null,"
                    + " (SELECT NAME FROM OBJTYPE WHERE OBJECT_TYPE_ID = ?))";
    
    /**DELETE any entity. PreparedStatement args order: object_id, object_type_id*/
    public static final String DELETE_OBJECT = "DELETE objects WHERE object_id = ? AND object_type_id = ?";
    
    /** Put entity to container with checking if the correct parameter id of container you have.<br>
     * PreparedStatement args order:<br>
     * OBJECT_ID of container<br>
     * OBJECT_ID of entity<br>
     * OBJECT_ID of container<br>
     * OBJECT_ID of container<br>
     * OBJECT_TYPE_ID of container<br>
     * */
    public static final String PUT_ENTITY_TO_CONTAINER = 
            "UPDATE OBJECTS SET PARENT_ID = ?"
                    + " WHERE OBJECT_ID = ? AND ? ="
                    + "     (" + CHECK_OBJECT + ")";
    /**
     * This query allows to get value of specified attribute of given entity.
     * PreparedStatement args:
     * OBJECT_ID of entity
     * ATTR_ID of attribute
     */
    public static final String GET_ATTR_VALUE =
            "SELECT value FROM attributes_value WHERE object_id = ? and attr_id = ? ";
}
