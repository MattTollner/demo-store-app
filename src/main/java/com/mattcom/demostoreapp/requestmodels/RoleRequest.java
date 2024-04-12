package com.mattcom.demostoreapp.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleRequest {


    private int id;
    private String roleName;

    public RoleRequest(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

}
