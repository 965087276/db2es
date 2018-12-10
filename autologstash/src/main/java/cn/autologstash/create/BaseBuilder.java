package cn.autologstash.create;

public abstract class BaseBuilder {
    public abstract String build();

    public String indent(int p) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p; i++)
            sb.append("    ");
        return sb.toString();
    }
}
