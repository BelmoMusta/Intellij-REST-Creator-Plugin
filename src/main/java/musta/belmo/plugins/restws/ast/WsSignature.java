package musta.belmo.plugins.restws.ast;

import musta.belmo.plugins.restws.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WsSignature {
    private final WsURL wsURL;
    private final List<String> imports = new ArrayList<>();

    public WsSignature(String url) {
        imports.add(Constants.RESPONSE_ENTITY);
        this.wsURL = new WsURL(url);
        if (!wsURL.isValid()) {
            return;
        }
        this.addRequestBody();
        this.addPathVariableImport();
        this.addRequestParamImport();
    }

    public List<WsParam> getWsParams() {
        return wsURL.getParams();
    }

    public String getReturnType() {
        if ("put,post,patch,delete".contains(this.wsURL.getMethod().toLowerCase())) {
            return Constants.RESPONSE_ENTITY + "<Void>";
        }
        if (!this.wsURL.getPathParams().isEmpty()) {
            return Constants.RESPONSE_ENTITY + "<?>";
        } else {
            imports.add(Constants.JAVA_UTIL_LIST);
            return Constants.RESPONSE_ENTITY + "<" + Constants.GENERIC_JAVA_UTIL_LIST + ">";
        }
    }

    public List<String> getImports() {
        return imports;
    }

    public String getOriginalRawUrl() {
        return wsURL.getRaw();
    }

    private void addPathVariableImport() {
        List<WsParam> params = wsURL.getParams();
        boolean isPathParamExist = params.stream()
                .anyMatch(param -> param.getKind() == WsParam.ParamKind.PATH);
        if (isPathParamExist) {
            this.imports.add(Constants.PATH_VARIABLE);
        }
    }
    private void addRequestParamImport() {
        List<WsParam> params = wsURL.getParams();
        boolean isPathParamExist = params.stream()
                .anyMatch(param -> param.getKind() == WsParam.ParamKind.QUERY);
        if (isPathParamExist) {
            this.imports.add(Constants.REQUEST_PARAM);
        }
    }

    public String getMethodName() {
        final List<String> urlParts = this.wsURL.getUrlParts();

        String verb = wsURL.getVerbFromMethod()
                + urlParts.stream()
                .filter(res -> !res.contains("{")
                        && !res.contains(":") && !res.isEmpty())
                .map(TextUtils::capitalize)
                .collect(Collectors.joining());

        String methodName;
        if (!this.wsURL.getPathParams().isEmpty()) {
            methodName = verb + "By" + String.join("And", TextUtils.capitalize(this.wsURL.getPathParams()));
        } else {
            methodName = verb;
        }
        methodName = methodName.replaceAll("[. -=]", "_");
        return methodName;
    }

    public String getAnnotation() {
        String method = this.wsURL.getMethod();
        String annotationFromHttpMethod = getAnnotationFromHttpMethod(method);
        this.imports.add(annotationFromHttpMethod);
        return "@" + annotationFromHttpMethod + "(\"" + wsURL.getPath() + "\")";
    }
    private void addRequestBody() {

        if ("put,post,patch,delete".contains(this.wsURL.getMethod().toLowerCase())) {
            WsParam param = new WsParam();
            param.setName("requestBody");
            param.setType("java.lang.Object");
            this.imports.add(Constants.REQUEST_BODY);
            param.setAnnotation(Constants.REQUEST_BODY);
            this.getWsParams().add(param);
        }
    }
    private static String getAnnotationFromHttpMethod(String httpMethod) {
        return "org.springframework.web.bind.annotation."
                + TextUtils.capitalize(httpMethod.toLowerCase())
                + "Mapping";
    }
    public String getMethodComment() {
        return "//FIXME: Provide a valid implementation for this method";
    }
    public String getMethodBody() {
        final String statement;
        if ("get".equalsIgnoreCase(wsURL.getMethod())) {
            statement = "throw new RuntimeException(\"Not implemented yet\");";
        } else if ("post".equalsIgnoreCase(wsURL.getMethod())) {
            statement = "return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();";
        } else {
            statement = "return ResponseEntity.status(org.springframework.http.HttpStatus.NO_CONTENT).build();";
        }

        return statement;
    }
    public boolean isValid() {
        return wsURL.isValid();
    }
}
