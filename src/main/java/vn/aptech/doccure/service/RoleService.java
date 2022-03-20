package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Role;

public interface RoleService {
    Role findByName(String name);

    Role save(Role role);

    Iterable<Role> findAll();
}