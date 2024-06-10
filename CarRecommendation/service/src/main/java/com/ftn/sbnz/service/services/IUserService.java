package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.User;

public interface IUserService extends IBaseService<User> {
    boolean userExists(String email);
}
