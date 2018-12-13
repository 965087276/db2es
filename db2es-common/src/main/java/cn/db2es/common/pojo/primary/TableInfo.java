package cn.db2es.common.pojo.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * @author wangjinhao
 * @date 2018/11/27
 */
@Data
@Entity
@Table(name = "table_info")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TableInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "table_name")
    private String tableName;

    @ManyToOne
    @JoinColumn(name = "index_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private IndexInfo indexInfo;

    @Transient
    private List<TableField> tableFields;
    @Transient
    private List<TableConstraint> tableConstraints;

}
