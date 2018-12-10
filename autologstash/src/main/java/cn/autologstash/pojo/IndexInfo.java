package cn.autologstash.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author wangjinhao
 * @date 2018/11/27
 */
@Data
@Entity
@Table(name = "index_info")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class IndexInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "index_name")
    private String indexName;

    @Column(name = "is_built")
    private boolean built;

    @Column(name = "id_field")
    private String idField;

    @Transient
    List<IndexField> indexFields;
}
