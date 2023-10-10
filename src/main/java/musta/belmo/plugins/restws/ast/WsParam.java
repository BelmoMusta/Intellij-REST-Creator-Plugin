package musta.belmo.plugins.restws.ast;

public class WsParam {
    private String name;
    private String annotation;
    private String type;
    private ParamKind kind;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnnotation() {
        return "@"+annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    public boolean isIdentifier() {
        return name != null && name.matches("^([a-zA-Z_$][a-zA-Z\\d_$]*)$");
    }

    public void setKind(ParamKind kind) {
        this.kind = kind;
    }
    public ParamKind getKind() {
        return kind;
    }
    enum ParamKind{
        PATH,
        QUERY
    }
}
