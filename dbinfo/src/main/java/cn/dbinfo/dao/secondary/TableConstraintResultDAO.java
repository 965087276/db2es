package cn.dbinfo.dao.secondary;

import cn.dbinfo.pojo.secondary.TableConstraintResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TableConstraintResultDAO extends JpaRepository<TableConstraintResult, String> {
    /**
     *  find primary key by database and table
     * @param database database name
     * @param table table name
     * @return object:primary field name
     */
    @Query(value = "select t.COLUMN_NAME from KEY_COLUMN_USAGE t where " +
            "t.CONSTRAINT_NAME = 'PRIMARY' and t.TABLE_SCHEMA = ?1 and t.TABLE_NAME = ?2", nativeQuery = true)
    List<Object> findPKByDatabaseAndTable(String database, String table);

    /**
     * find foreign by database and table
     * @param database database name
     * @param table table name
     * @return  object[0]:fk_database; object[1]:fk_table; object[2]:fk_field
     */
    @Query(value = "select t.COLUMN_NAME, t.REFERENCED_TABLE_SCHEMA, t.REFERENCED_TABLE_NAME, t.REFERENCED_COLUMN_NAME from KEY_COLUMN_USAGE t where " +
            "t.REFERENCED_TABLE_NAME is not null and t.TABLE_SCHEMA = ?1 and t.TABLE_NAME = ?2", nativeQuery = true)
    List<Object[]> findFkByDatabaseAndTable(String database, String table);
}