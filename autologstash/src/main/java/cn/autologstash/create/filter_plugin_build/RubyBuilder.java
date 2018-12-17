package cn.autologstash.create.filter_plugin_build;

import cn.autologstash.create.BaseBuilder;
import org.springframework.stereotype.Component;

@Component("rubyBuilder")
public class RubyBuilder extends BaseBuilder {
    @Override
    public String build() {
        return "    ruby {\n" +
                "       code => \"\n" +
                "           str_source = ''\n" +
                "           event.get(event.get([@metadata][father])).each do |col|\n" +
                "               event.set(col['new_name'], col['value'])\n" +
                "           end\n" +
                "        \"\n" +
                "    }\n" +
                "    \n" +
                "    if [@metadata][index_update] and [@metadata][index_update] != \"drop it\" {\n" +
                "        ruby {\n" +
                "            code => \"\n" +
                "                str_source = ''\n" +
                "                    event.get(event.get([@metadata][father])).each do |col|\n" +
                "                        str_source << \\\"ctx._source.#{col['new_name']} = #{col['value']};\\\"\n" +
                "                        if col['new_name'] == [@metadata][index_id]\n" +
                "                            event.set(\\\"[update_by_query][query][terms][#{col['new_name']}]\\\", col['value'])\n" +
                "                        end\n" +
                "                    end\n" +
                "                event.set('[update_by_query][script][source]', str_source)\n" +
                "                event.set('[update_by_query][script][lang]', 'painless')\n" +
                "             \"\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    if [head][type] == \"INSERT\" {\n" +
                "        mutate {\n" +
                "            remove_field => [\"after\", \"head\"]\n" +
                "            add_field => { \"[@metadata][op_type]\" => \"index\" }\n" +
                "        }\n" +
                "    } else if [head][type] == \"UPDATE\" {\n" +
                "        mutate {\n" +
                "            remove_field => [\"before\", \"after\", \"head\"]\n" +
                "            add_field => { \"[@metadata][op_type]\" => \"index\" }\n" +
                "        }\n" +
                "    } else if [head][type] == \"DELETE\" {\n" +
                "        mutate {\n" +
                "            remove_field => [\"before\", \"head\"]\n" +
                "            add_field => { \"[@metadata][op_type]\" => \"delete\" }\n" +
                "        }\n" +
                "    } else {\n" +
                "        drop {}\n" +
                "    }\n";
    }
}
