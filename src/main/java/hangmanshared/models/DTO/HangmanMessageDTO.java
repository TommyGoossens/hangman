package hangmanshared.models.DTO;

import hangmanshared.models.enums.HangmanMessageOperation;

public class HangmanMessageDTO {
    private HangmanMessageOperation operation;
    private String property;
    private String content;

    public HangmanMessageDTO() {
    }

    public HangmanMessageDTO(HangmanMessageOperation operation) {
        this.operation = operation;
        }

    public HangmanMessageOperation getOperation() {
        return operation;
    }

    public void setOperation(HangmanMessageOperation operation) {
        this.operation = operation;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
