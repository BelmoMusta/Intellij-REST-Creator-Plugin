package musta.belmo.plugins.restws.ast;

import musta.belmo.plugins.restws.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WsURL {
    private final String raw;
    private final String method;
    private final String path;
    private final List<String> urlParts;
    private final String query;
    private final List<WsParam> params = new ArrayList<>();

    public WsURL(String raw) {
        this.raw = raw;
        this.method = raw.split("\\s+")[0];
        String split = raw.split("\\s+")[1];
        this.path = TextUtils.getPath(split);
        String temp = TextUtils.mapPathVariablesToTheirResources(path);
        this.urlParts = Arrays.stream(temp.split("/"))
                .filter(tkn -> !tkn.isEmpty())
                .toList();
        this.query = TextUtils.getQuery(split.trim());
        getPathVariablesParams();
        getQueryParams(split);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getRaw() {
        return raw;
    }

    public List<String> getUrlParts() {
        return urlParts;
    }

    public List<String> getPathParams() {
        return params.stream()
                .filter(param -> param.getKind() == WsParam.ParamKind.PATH)
                .map(WsParam::getName)
                .toList();
    }

    public String getVerbFromMethod() {
        String verb = method.toLowerCase();
        if ("post".equalsIgnoreCase(method)) {
            verb = "create";
        } else if ("put".equalsIgnoreCase(method)) {
            verb = "update";
        } else if ("patch".equalsIgnoreCase(method)) {
            verb = "edit";
        }
        return verb;
    }

    private void getPathVariablesParams() {
        Map<String, String> mappedPathVariables = TextUtils.getMappedPathVariables(path);
        for (Map.Entry<String, String> keValue : mappedPathVariables.entrySet()) {
            String pathVariableName = keValue.getKey();
            final WsParam param = new WsParam();
            param.setAnnotation(Constants.PATH_VARIABLE + "(\"" + pathVariableName + "\")");
            String paramName = keValue.getValue();
            param.setName(paramName);
            if (paramName.toLowerCase().contains("id")) {
                param.setType("java.lang.Long");
            } else {
                param.setType("java.lang.String");
            }
            param.setKind(WsParam.ParamKind.PATH);

            params.add(param);
        }
    }

    private void getQueryParams(String split) {
        Map<String, String> queryParams = TextUtils.getQueryParams(split.trim());
        for (Map.Entry<String, String> keyValue : queryParams.entrySet()) {
            final WsParam param = new WsParam();
            param.setType("java.lang.String");
            param.setName(keyValue.getKey());
            param.setKind(WsParam.ParamKind.QUERY);
            param.setAnnotation(Constants.REQUEST_PARAM + "(\"" + keyValue.getKey() + "\")");
            params.add(param);
        }
    }
    public boolean isValid() {
        for (WsParam wsParam : params) {
            if (!wsParam.isIdentifier()) {
                return false;
            }
        }
        return true;
    }

    public List<WsParam> getParams() {
        return params;
    }
}
