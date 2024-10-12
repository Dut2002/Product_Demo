package com.example.demo_oracle_db.service.login.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Res {
    String status;
    String title;
    Object content;

    public Res resOk(Object content) {
        this.status = Constants.ApiStatus.SUCCESS;
        this.title = Constants.ApiStatus.SUCCESS;
        this.content = content;
        return this;
    }

    public Res resOk() {
        this.status = Constants.ApiStatus.SUCCESS;
        this.title = Constants.ApiStatus.SUCCESS;
        this.content = null; // Hoặc có thể đặt thành một thông điệp mặc định
        return this;
    }

    public Res resError(String title, Object content) {
        this.status = Constants.ApiStatus.ERROR;
        this.title = title;
        this.content = content;
        return this;
    }
}
