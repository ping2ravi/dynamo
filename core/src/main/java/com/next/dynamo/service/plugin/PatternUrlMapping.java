package com.next.dynamo.service.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.next.dynamo.persistance.UrlMapping;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class PatternUrlMapping {
    private final UrlMapping urlMapping;
    private Pattern pattern;
    private final List<String> parameters;
    private final Set<String> aliases;
    private final List<WebDataPlugin> dataPlugins;


    public PatternUrlMapping(UrlMapping urlMapping, List<WebDataPlugin> dataPlugins) {
        this.dataPlugins = dataPlugins;
        this.urlMapping = urlMapping;
        parameters = new ArrayList<String>();
        aliases = urlMapping.getAliases();

        pattern = null;
        // Build parameters and pattern
        char[] charArray = urlMapping.getUrlPattern().toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean paramStarted = false;
        StringBuilder paramNameBuilder = new StringBuilder();
        for (char oneChar : charArray) {
            if (oneChar == '{') {
                paramStarted = true;
                paramNameBuilder = new StringBuilder();
                continue;
            }
            if (oneChar == '}') {
                paramStarted = false;
                sb.append("(.*)");
                parameters.add(paramNameBuilder.toString());
                continue;
            }
            if (paramStarted) {
                paramNameBuilder.append(oneChar);
                continue;
            }
            sb.append(oneChar);
        }
        if (!parameters.isEmpty()) {
            log.info("sb = " + sb.toString());
            pattern = Pattern.compile(sb.toString());
        }
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((urlMapping.getUrlPattern() == null) ? 0 : urlMapping.getUrlPattern().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PatternUrlMapping other = (PatternUrlMapping) obj;
        if (urlMapping.getUrlPattern() == null) {
            if (other.urlMapping.getUrlPattern() != null)
                return false;
        } else if (!urlMapping.getUrlPattern().equals(other.urlMapping.getUrlPattern()))
            return false;
        return true;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<WebDataPlugin> getDataPlugins() {
        return dataPlugins;
    }

    public Set<String> getAliases() {
        return aliases;
    }

}
