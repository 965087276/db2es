package cn.dbinfo.pojo.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * @author wangjinhao
 * @date 2018/11/27
 */
@Data
@Entity
@Table(name = "table_constraint")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer", "PRIMARY_KEY", "FOREIGN_KEY" })
public class TableConstraint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private TableInfo tableInfo;

    private String name;
    private int type;

    @Column(name = "fk_database")
    private String fkDatabase;

    @Column(name = "fk_table")
    private String fkTable;

    @Column(name = "fk_field")
    private String fkField;

    @Transient
    public static int PRIMARY_KEY = 0;
    @Transient
    public static int FOREIGN_KEY = 1;
}
