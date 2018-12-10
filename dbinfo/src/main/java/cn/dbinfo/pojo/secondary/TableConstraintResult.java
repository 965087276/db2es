package cn.dbinfo.pojo.secondary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "KEY_COLUMN_USAGE")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TableConstraintResult {
    @Id
    @Column(name = "TABLE_SCHEMA")
    private String database;

    @Column(name = "TABLE_NAME")
    private String table;

    @Column(name = "CONSTRAINT_NAME")
    private String constraintName;

    @Column(name = "REFERENCED_TABLE_SCHEMA")
    private String fkDatabase;

    @Column(name = "REFERENCED_TABLE_NAME")
    private String fkTable;

    @Column(name = "REFERENCED_COLUMN_NAME")
    private String fkField;
}
