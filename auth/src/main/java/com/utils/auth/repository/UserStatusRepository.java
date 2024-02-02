package com.utils.auth.repository;
import com.utils.auth.model.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserStatusRepository extends MongoRepository<UserStatus,String> {
    Optional<UserStatus> findUserStatusByEmail(String email);
}
