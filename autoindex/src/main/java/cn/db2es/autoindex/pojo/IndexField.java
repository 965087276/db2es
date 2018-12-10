package cn.db2es.autoindex.pojo;

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
@Table(name = "index_field")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class IndexField implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "index_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private IndexInfo indexInfo;

    @ManyToOne
    @JoinColumn(name = "father_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private IndexField father;

    @Transient
    List<IndexField> children;

    @Column(name = "table_field")
    private String tableField;

    private String name;
    private String type;
    private String analyzer;
    private String format;
    private double boost = 1;
    @Column(name = "is_analyze")
    private boolean analyzed = true;
    @Column(name = "is_search")
    private boolean searched = true;

    @Override
    public Object clone() {
        IndexField indexField = null;
        try{
            indexField = (IndexField) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return indexField;
    }

}
