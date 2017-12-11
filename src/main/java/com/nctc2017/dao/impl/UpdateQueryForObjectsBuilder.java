package com.nctc2017.dao.impl;

import com.sun.istack.internal.NotNull;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateQueryForObjectsBuilder {

    private static final String OBJECT_TYPE_ID = "objectTypeId";
    private static final String OBJECT_ID = "objectId";
    private static final String PARENT_ID = "parentId";
    private static final String SOURCE_OBJECT_ID = "sourceObjId";

    private final Operation queryOperation;
    private Map<String, BigDecimal> objectColumnsValues;
    private Map<BigDecimal, String> attributes;
    private Map<BigDecimal, String> dateAttributes;

    private enum Operation {
        INSERT, UPDATE_OBJECT_PARENT_ID, UPDATE_OBJECT_ATTRIBUTE_VALUE, DELETE
    }

    private UpdateQueryForObjectsBuilder(Operation operation) {
        queryOperation = operation;
        objectColumnsValues = new HashMap<>();
        attributes = new HashMap<>();
        dateAttributes = new HashMap<>();
    }

    private UpdateQueryForObjectsBuilder(Operation operation, BigInteger objectId) {
        this(operation);
        this.setObjectId(objectId);
    }

    /**
     * Creates builder for inserting object and its attributes values.
     *
     * @return builder
     */
    public static UpdateQueryForObjectsBuilder newInsertBuilder(@NotNull BigInteger objectTypeId) {
        UpdateQueryForObjectsBuilder builder = new UpdateQueryForObjectsBuilder(Operation.INSERT);
        builder.setObjectTypeId(objectTypeId);

        return builder;
    }

    /**
     * Creates builder for updating object parent_id
     *
     * @param objectId - the object for which you want to update parent_id
     * @param parentId - id of new parent object
     * @return builder
     */
    public static UpdateQueryForObjectsBuilder newUpdateParentIdBuilder(@NotNull BigInteger objectId, BigInteger parentId) {
        UpdateQueryForObjectsBuilder builder = new UpdateQueryForObjectsBuilder(Operation.UPDATE_OBJECT_PARENT_ID, objectId);
        builder.setParentId(parentId);

        return builder;
    }

    /**
     * Creates builder for updating object attributes values. Can update several attributes at once.
     *
     * @param objectId - the object for which you want to update attributes values
     * @return builder
     */
    public static UpdateQueryForObjectsBuilder newUpdateAttributeValueBuilder(@NotNull BigInteger objectId) {
        return new UpdateQueryForObjectsBuilder(Operation.UPDATE_OBJECT_ATTRIBUTE_VALUE, objectId);
    }

    /**
     * Creates builder for delete specified object
     *
     * @param objectId - the object which you want to delete
     * @return builder
     */
    public static UpdateQueryForObjectsBuilder newDeleteBuilder(@NotNull BigInteger objectId) {
        return new UpdateQueryForObjectsBuilder(Operation.DELETE, objectId);
    }


    private void setObjectId(BigInteger id) {
        objectColumnsValues.put(OBJECT_ID, new BigDecimal(id));
    }

    /**
     * Set object type id for new object
     *
     * @param id - object type id of object that will be updated in query
     * @return builder
     */
    public UpdateQueryForObjectsBuilder setObjectTypeId(BigInteger id) {
        if (objectColumnsValues.containsKey(OBJECT_TYPE_ID)) {
            objectColumnsValues.replace(OBJECT_TYPE_ID, new BigDecimal(id));
        } else {
            objectColumnsValues.put(OBJECT_TYPE_ID, new BigDecimal(id));
        }
        return this;
    }

    /**
     * Set parent id
     *
     * @param id - parent id for object that will be updated in query
     * @return builder
     */
    public UpdateQueryForObjectsBuilder setParentId(BigInteger id) {
        if (objectColumnsValues.containsKey(PARENT_ID)) {
            objectColumnsValues.replace(PARENT_ID, new BigDecimal(id));
        } else {
            objectColumnsValues.put(PARENT_ID, new BigDecimal(id));
        }
        return this;
    }

    /**
     * Set source object id
     *
     * @param id - source object id for object that will be updated in query
     * @return builder
     */
    public UpdateQueryForObjectsBuilder setSourceObjId(BigInteger id) {
        if (objectColumnsValues.containsKey(SOURCE_OBJECT_ID)) {
            objectColumnsValues.replace(SOURCE_OBJECT_ID, new BigDecimal(id));
        } else {
            objectColumnsValues.put(SOURCE_OBJECT_ID, new BigDecimal(id));
        }
        return this;
    }

    /**
     * Set new value for attribute with id = attributeId
     *
     * @param attributeId    - id of attribute that will be updated in query
     * @param attributeValue - attribute value that will be set in query
     * @return builder
     */
    public UpdateQueryForObjectsBuilder setAttribute(BigInteger attributeId, String attributeValue) {
        if (attributes.containsKey(attributeId)) {
            attributes.replace(new BigDecimal(attributeId), attributeValue);
        } else {
            attributes.put(new BigDecimal(attributeId), attributeValue);
        }
        return this;
    }

    /**
     * Set new date value for attribute with id = attributeId
     *
     * @param attributeId        - id of attribute that will be updated in query
     * @param dateAttributeValue - attribute date value that will be set in query
     * @return builder
     */
    public UpdateQueryForObjectsBuilder setDateAttribute(BigInteger attributeId, String dateAttributeValue) {
        if (dateAttributes.containsKey(attributeId)) {
            dateAttributes.replace(new BigDecimal(attributeId), dateAttributeValue);
        } else {
            dateAttributes.put(new BigDecimal(attributeId), dateAttributeValue);
        }
        return this;
    }

    /**
     * Build the update query
     *
     * @return preparedStatement with query
     */
    public PreparedStatementCreator build() {

        ArrayList<SqlParameter> declaredParams = new ArrayList<>();
        SqlParameter numericParam = new SqlParameter(Types.NUMERIC);
        SqlParameter varcharParam = new SqlParameter(Types.VARCHAR);
        SqlParameter nullParam = new SqlParameter(Types.NULL);

        switch (queryOperation) {

            case INSERT:

                StringBuilder attributesQuery = new StringBuilder();
                String oneAttributeQuery = " INTO attributes_value(attr_id, object_id, value, date_value) VALUES ( ? , obj_sq.currval, ?, ? ) ";
                for (int i = 0; i < attributes.size() + dateAttributes.size(); i++) {
                    attributesQuery.append(oneAttributeQuery);
                }

                String insertObjectQuery = "INSERT ALL INTO objects (object_id, parent_id, object_type_id, source_id, name)" +
                        " values (obj_sq.nextval, ?, ?, ?, ";

                String objectNameQuery;
                if (objectColumnsValues.containsKey(SOURCE_OBJECT_ID)) {
                    objectNameQuery = "( SELECT name FROM objects WHERE object_id = ?)) ";
                } else {
                    objectNameQuery = "( SELECT name FROM objtype WHERE object_type_id = ?)) ";
                }

                String selectQuery = " SELECT 1 FROM dual ";

                ArrayList<Object> paramsInsert = new ArrayList<>();

                paramsInsert.add(objectColumnsValues.getOrDefault(PARENT_ID, null));
                declaredParams.add(numericParam);
                paramsInsert.add(objectColumnsValues.getOrDefault(OBJECT_TYPE_ID, null));
                declaredParams.add(numericParam);
                paramsInsert.add(objectColumnsValues.getOrDefault(SOURCE_OBJECT_ID, null));
                declaredParams.add(numericParam);
                paramsInsert.add(objectColumnsValues.getOrDefault(SOURCE_OBJECT_ID, objectColumnsValues.get(OBJECT_TYPE_ID)));
                declaredParams.add(numericParam);

                for (Map.Entry<BigDecimal, String> cursor : attributes.entrySet()) {
                    paramsInsert.add(cursor.getKey());
                    declaredParams.add(numericParam);
                    paramsInsert.add(cursor.getValue());
                    declaredParams.add(varcharParam);
                    paramsInsert.add(null); // date_value is always null here
                    declaredParams.add(nullParam);
                }
                for (Map.Entry<BigDecimal, String> cursor : dateAttributes.entrySet()) {
                    paramsInsert.add(cursor.getKey());
                    declaredParams.add(numericParam);
                    paramsInsert.add(null); // value is always null here
                    declaredParams.add(nullParam);
                    paramsInsert.add(cursor.getValue());
                    declaredParams.add(varcharParam);
                }

                PreparedStatementCreatorFactory stmtInsert = new PreparedStatementCreatorFactory(
                        insertObjectQuery + objectNameQuery + attributesQuery.toString() + selectQuery,
                        declaredParams);
                return stmtInsert.newPreparedStatementCreator(paramsInsert);


            case UPDATE_OBJECT_PARENT_ID:
                String updateObjectQuery = "UPDATE objects SET parent_id = ? WHERE object_id = ?";

                ArrayList<Object> paramsUpdateParent = new ArrayList<>();
                paramsUpdateParent.add(objectColumnsValues.getOrDefault(PARENT_ID, null));
                declaredParams.add(numericParam);
                paramsUpdateParent.add(objectColumnsValues.get(OBJECT_ID));
                declaredParams.add(numericParam);

                PreparedStatementCreatorFactory stmtUpdateParent = new PreparedStatementCreatorFactory(updateObjectQuery, declaredParams);
                return stmtUpdateParent.newPreparedStatementCreator(paramsUpdateParent);


            case UPDATE_OBJECT_ATTRIBUTE_VALUE:

                StringBuilder updateAttrQuery = new StringBuilder("BEGIN ");

                String oneAttrForUpdateQuery = "UPDATE attributes_value SET value = ? WHERE object_id = ? and attr_id = ? ;";
                for (int i = 0; i < attributes.size(); i++) {
                    updateAttrQuery.append(oneAttrForUpdateQuery);
                }

                String oneDateAttrForUpdateQuery = "UPDATE attributes_value SET date_value = ? WHERE object_id = ? and attr_id = ? ;";
                for (int i = 0; i < dateAttributes.size(); i++) {
                    updateAttrQuery.append(oneDateAttrForUpdateQuery);
                }

                ArrayList<Object> paramsUpdateAttr = new ArrayList<>();

                for (Map.Entry<BigDecimal, String> cursor : attributes.entrySet()) {
                    paramsUpdateAttr.add(cursor.getValue());
                    declaredParams.add(varcharParam);
                    paramsUpdateAttr.add(objectColumnsValues.get(OBJECT_ID));
                    declaredParams.add(numericParam);
                    paramsUpdateAttr.add(cursor.getKey());
                    declaredParams.add(numericParam);
                }

                for (Map.Entry<BigDecimal, String> cursor : dateAttributes.entrySet()) {
                    paramsUpdateAttr.add(cursor.getValue());
                    declaredParams.add(varcharParam);
                    paramsUpdateAttr.add(objectColumnsValues.get(OBJECT_ID));
                    declaredParams.add(numericParam);
                    paramsUpdateAttr.add(cursor.getKey());
                    declaredParams.add(numericParam);
                }

                PreparedStatementCreatorFactory stmtUpdateAttr = new PreparedStatementCreatorFactory(updateAttrQuery.toString() + " END;", declaredParams);
                return stmtUpdateAttr.newPreparedStatementCreator(paramsUpdateAttr);


            case DELETE:

                String deleteObjectQuery = "delete from objects where object_id = ?";

                ArrayList<Object> paramsDeleteObject = new ArrayList<>();
                paramsDeleteObject.add(objectColumnsValues.get(OBJECT_ID));
                declaredParams.add(numericParam);

                PreparedStatementCreatorFactory stmtDeleteObject = new PreparedStatementCreatorFactory(deleteObjectQuery, declaredParams);
                return stmtDeleteObject.newPreparedStatementCreator(paramsDeleteObject);

            default:
                return null;
        }

    }

}
