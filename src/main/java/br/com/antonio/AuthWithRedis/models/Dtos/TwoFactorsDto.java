package br.com.antonio.AuthWithRedis.models.Dtos;


public record TwoFactorsDto(String email, String code) {

}
