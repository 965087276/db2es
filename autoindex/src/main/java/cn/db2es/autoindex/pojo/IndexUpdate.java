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
@Table(name = "index_update")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class IndexUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "index_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private IndexInfo indexInfo;

    @Column(name = "update_index")
    private String updateIndex;
}
