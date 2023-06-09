package com.example.top.dto.employee;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Long roleId;

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}
