package cn.autologstash.create.filter_plugin_build;

import cn.autologstash.create.BaseBuilder;
import cn.autologstash.remote.DbinfoRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("filterPluginBuilder")
public class FilterPluginBuilder extends BaseBuilder {
    @Autowired
    private MapFileBuilder mapFileBuilder;
    @Autowired
    private RubyBuilder rubyBuilder;
    @Autowired
    private ForeignKeyBuilder foreignKeyBuilder;

    @Override
    public String build() {
        StringBuffer sb = new StringBuffer();
        sb.append("filter {" + "\n")
                .append(mapFileBuilder.build())
                .append(rubyBuilder.build())
                .append(foreignKeyBuilder.build())
                .append("}" + "\n");
        return sb.toString();
    }

}
