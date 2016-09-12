package com.next.dynamo.service.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractDataPlugin implements WebDataPlugin {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
    protected SimpleDateFormat ddMMMyyyyFormat = new SimpleDateFormat("dd-MMM-yyyy");
    protected SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy");
    protected SimpleDateFormat ddMMyyyyHHMMFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private Gson gson = new Gson();

    Map<String, String> settingMap = new LinkedHashMap<String, String>();
    protected final String name;

    public AbstractDataPlugin() {
        this("NoName");
    }

    public AbstractDataPlugin(String pluginName) {
        this.name = pluginName;
    }

    protected Pageable getPageRequest(HttpServletRequest httpServletRequest) {
        int page = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
        int size = getIntSettingPramater("news.size", 2);// getIntPramater(httpServletRequest, HttpParameters.PAGE_SIZE_PARAM, HttpParameters.PAGE_SIZE_DEFAULT_VALUE);
        Pageable pageable = new PageRequest(page, size);
        return pageable;
    }

    @Override
    public void setSettings(String settings) {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject settingJsonObject = (JsonObject) jsonParser.parse(settings);
            addSettingToMap(settingJsonObject, null, settingMap);

        } catch (Exception ex) {
            logger.error("Invalid Setting for Plugin {}", name);
        }

    }

    private void addSettingToMap(JsonObject jsonObject, String prefix, Map<String, String> settingMap) {
        for(Entry<String, JsonElement> oneEntry : jsonObject.entrySet()){
            if (oneEntry.getValue().isJsonPrimitive()) {
                String propertyName;
                if (prefix == null) {
                    propertyName = oneEntry.getKey();
                } else {
                    propertyName = prefix + "." + oneEntry.getKey();
                }
                settingMap.put(propertyName, oneEntry.getValue().getAsString());
            } else {
                if (prefix == null) {
                    addSettingToMap(oneEntry.getValue().getAsJsonObject(), oneEntry.getKey(), settingMap);
                } else {
                    addSettingToMap(oneEntry.getValue().getAsJsonObject(), prefix + "." + oneEntry.getKey(), settingMap);
                }

            }
        }
    }

    protected int getIntSettingPramater(String paramName, int defaultValue) {
        try {
            return Integer.parseInt(settingMap.get(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected Long getLongSettingPramater(String paramName, Long defaultValue) {
        try {
            return Long.parseLong(settingMap.get(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected String getStringSettingPramater(String paramName, String defaultValue) {
        String paramValue = settingMap.get(paramName);
        if (paramValue == null) {
            paramValue = defaultValue;
        }
        return paramValue;
    }
    protected int getIntPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Integer.parseInt(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected long getLongPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Long.parseLong(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public String getName() {
        return name;
    }

    public void addDateField(JsonObject jsonObject, String fieldName, Date fieldValue) {
        if (fieldValue == null) {
            jsonObject.addProperty(fieldName, "");
            return;
        }
        jsonObject.addProperty(fieldName, fmt.print(fieldValue.getTime()));
    }

    protected Long getLongParameterFromPathOrParams(HttpServletRequest httpServletRequest, String paramName) {
        String paramIdStr = getStringParameterFromPathOrParams(httpServletRequest, paramName);
        try {
            return Long.parseLong(paramIdStr);
        } catch (Exception ex) {
            return null;
        }
    }

    protected String getStringParameterFromPathOrParams(HttpServletRequest httpServletRequest, String paramName) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        
        String paramStr = pathParams.get(paramName);
        if (paramStr == null) {
            paramStr = httpServletRequest.getParameter(paramName);
        }
        return paramStr;
    }

}
