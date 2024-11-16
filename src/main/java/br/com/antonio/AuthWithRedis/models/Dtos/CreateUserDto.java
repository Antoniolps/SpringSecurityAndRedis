package br.com.antonio.AuthWithRedis.models.Dtos;

public record CreateUserDto(String email, String name, String password, String role) {

}
