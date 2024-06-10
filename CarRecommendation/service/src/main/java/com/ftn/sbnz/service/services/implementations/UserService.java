package com.ftn.sbnz.service.services.implementations;

import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.service.repositories.BaseJPARepository;
import com.ftn.sbnz.service.repositories.UserRepository;
import com.ftn.sbnz.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> implements IUserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected BaseJPARepository<User> getRepository() {
        return this.userRepository;
    }

    @Override
    public boolean userExists(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }
}
