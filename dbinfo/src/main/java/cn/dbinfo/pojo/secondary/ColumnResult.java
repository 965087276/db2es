package cn.dbinfo.pojo.secondary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "COLUMNS")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
/**
 * information_schame库中的COLUMNS表，存放数据表字段的一些基本信息
 * Id注解随便加的
 */
public class ColumnResult {
    @Id
    @Column(name = "TABLE_SCHEMA")
    private String database;
    @Column(name = "TABLE_NAME")
    private String table;
    @Column(name = "COLUMN_NAME")
    private String name;
    @Column(name = "DATA_TYPE")
    private String type;
}
