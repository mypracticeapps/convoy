package io.sskrishna.rest.response;

import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorCodeLookup {
    private Map<String, ErrorDetail> errorCodeMap = new HashMap<>();

    public ErrorCodeLookup(Resource resource) throws IOException {
        InputStream stream = resource.getInputStream();
        List<Map<String, String>> _conf = (List<Map<String, String>>) new Yaml().load(stream);
        for (Map<String, String> map : _conf) {
            this.toErrorCode(map);
        }
    }

    public ErrorDetail getErrorCode(String code) {
        ErrorDetail errorCode = this.errorCodeMap.get(code);
        if (errorCode == null) {
            throw new RuntimeException("Error code not found: " + code);
        }
        return errorCode;
    }

    private void toErrorCode(Map<String, String> map) {
        ErrorDetail errorCode = new ErrorDetail();
        errorCode.setCode(map.get("code"));
        errorCode.setMessage(map.get("message"));
        String devMsg = map.get("devMessage");
        if (devMsg != null && !devMsg.equals(errorCode.getMessage())) {
            errorCode.setDevMessage(map.get("devMessage"));
        }
        this.errorCodeMap.put(errorCode.getCode(), errorCode);
    }
}