package com.akbank.springjwt.dtos;

import lombok.Data;

@Data
public class TokenRequestDto {

  public String username;
  public String password;

}
