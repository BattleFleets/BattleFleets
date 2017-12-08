package com.nctc2017.constants;

public class Query {
	/**
	 * This query allows to get any Entity by OBJECT_TYPE_ID and OBJECT_ID.
	 * Call execute() or query() must have 4 parameters for PreparedStatement: ObjectTypeId, ObjectId, ObjectTypeId, ObjectId 
	 * */
	public static final String findAnyEntity = 
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
	 * Call execute() or query() must have 4 parameters for PreparedStatement: ObjectTypeId, ObjectIdContainer, ObjectTypeId, ObjectIdContainer 
	 * */
	public static final String getEntitiesFromContainer = 
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

}
