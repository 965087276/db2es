package cn.db2es.autoindex.pojo;

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
@Table(name = "table_field")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TableField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private TableInfo tableInfo;

    @ManyToOne
    @JoinColumn(name = "index_field_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private IndexField indexField;

    private String name;
    private String type;
}
