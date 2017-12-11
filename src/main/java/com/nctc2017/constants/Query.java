package com.nctc2017.constants;

import oracle.net.aso.f;

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
    
    public static final String GET_CURRVAL = "SELECT obj_sq.currval FROM DUAL";
    
    /**DELETE any entity. PreparedStatement args order: object_id, object_type_id*/
    public static final String DELETE_ENTITY = "DELETE objects WHERE object_id = ? AND object_type_id = ?";

    /**
     * This query allows to update any Attributes_Value by OBJECT_ID
     * Call update() must have 5 parameters for PreparedStatement:
     * Your new value, ATTRIBUTE_ID, OBJECT_ID, OBJECT_TYPE_ID,OBJECT_ID
     * */
    public static final String UPDATE_ONE_ATTRIBUTE_VALUE =
            "UPDATE  attributes_value" +
            "SET VALUE = ?" +
            "WHERE attributes_value.attr_id = ?" +
            "AND attributes_value.object_id = ?" +
            "AND EXISTS (" +
            "SELECT *" +
            "FROM objects o" +
            "WHERE o.object_type_id = ?" +
            "AND o.object_id = ?)";
}

