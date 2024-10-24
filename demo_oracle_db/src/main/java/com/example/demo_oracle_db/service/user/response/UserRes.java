package com.example.demo_oracle_db.service.user.response;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserRes {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Constants.Status status;
    private List<SearchBox> roles;

    public UserRes(Account user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.status = user.getStatus();
        this.roles = user.getAccountRoles().stream().map(
                accountRole -> new SearchBox(accountRole.getRole()))
                .toList();
    }

    public static Page<UserRes> MapPageUser(Page<Account> users) {
        List<UserRes> userRes = users.getContent().stream()
                .map(UserRes::new)
                .collect(Collectors.toList());
        return new PageImpl<>(userRes, users.getPageable(), users.getTotalElements());
    }
}
