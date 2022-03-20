package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.repository.RoleRepository;
import vn.aptech.doccure.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }
}