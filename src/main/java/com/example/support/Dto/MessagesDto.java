package com.example.support.Dto;

import com.example.support.models.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesDto {

    private Long chaID;
    private String userName;
    private String message;

    public static MessagesDto valueOf(Messages messages) {
        return new MessagesDto(
                messages.getId(),
                messages.getUserName(),
                messages.getMessage()
        );
    }
    public Messages mapTo() {
        return new Messages(chaID,userName,message);
    }


}
