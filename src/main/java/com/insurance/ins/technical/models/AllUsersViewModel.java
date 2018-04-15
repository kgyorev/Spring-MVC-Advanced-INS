package com.insurance.ins.technical.models;

import com.insurance.ins.technical.entites.User;
import org.springframework.data.domain.Page;

public class AllUsersViewModel {
    private Page<User> users;

    private long totalPageCount;

    public Page<User> getUsers() {
        return users;
    }

    public void setUsers(Page<User> users) {
        this.users = users;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
