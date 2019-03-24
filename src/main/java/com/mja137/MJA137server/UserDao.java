package com.mja137.MJA137server;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<UserEntity, String> {

    List<UserEntity> findByNameContainingIgnoreCase(String searchValue);
    List<UserEntity> findByIdContainingIgnoreCase(String searchValue);
}
