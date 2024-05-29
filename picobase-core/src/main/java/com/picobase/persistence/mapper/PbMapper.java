package com.picobase.persistence.mapper;

import com.picobase.persistence.dbx.Query;
import com.picobase.persistence.dbx.SelectQuery;
import com.picobase.persistence.dbx.expression.Expression;
import com.picobase.persistence.repository.PbRowMapper;

import static com.picobase.persistence.mapper.PbMapperManager.DEFAULT_DATA_SOURCE;

/**
 * The parent class of the all mappers.
 **/

public interface PbMapper {

    PbRowMapper getPbRowMapper();


    /**
     * Get the name of table.
     *
     * @return The name of table.
     */
    String getTableName();

    /**
     * Get the datasource name.
     *
     * @return The name of datasource.
     */
    default String getDataSource() {
        return DEFAULT_DATA_SOURCE;
    }


    <T> Class<T> getModelClass();

    SelectQuery modelQuery();

    SelectQuery findBy(Expression expression);

    Query insertQuery(Object data, String... includeFields);

    Query insertQuery(Object data, UpsertOptions options);

    Query delete(Expression where);

    Query updateQuery(Object data, Expression where, String... includeFields);

    Query updateQuery(Object data, Expression where, UpsertOptions options);
}