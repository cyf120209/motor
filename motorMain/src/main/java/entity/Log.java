package entity;

import java.io.Serializable;

public class Log implements Serializable{

    private Integer id;

    private String datetime;

    private Integer source;

    private Integer frametype;

    private Integer destination;

    private String content;

    private String contentReal;

    public Log() {
    }

    public Log(String datetime, Integer source, Integer frametype, Integer destination, String content, String contentReal) {
        this.datetime = datetime;
        this.source = source;
        this.frametype = frametype;
        this.destination = destination;
        this.content = content;
        this.contentReal = contentReal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getFrametype() {
        return frametype;
    }

    public void setFrametype(Integer frametype) {
        this.frametype = frametype;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentReal() {
        return contentReal;
    }

    public void setContentReal(String contentReal) {
        this.contentReal = contentReal;
    }
}
