package br.com.antonio.AuthWithRedis.models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnedUserDto {
    private String email;
    private String name;
}
