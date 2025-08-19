package com.zedas.asset.tools.jira.worklog.tool.model.jira.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Comment {
    
    @SerializedName("version")
    @Expose
    private Integer version;
    
    @SerializedName("type")
    @Expose
    private String type;
    
    @SerializedName("content")
    @Expose
    private List<Object> content;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getContent() {
        return content;
    }

    public void setContent(List<Object> content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        // Fallback dla kompatybilności z istniejącym kodem
        return content != null ? content.toString() : "";
    }
}
