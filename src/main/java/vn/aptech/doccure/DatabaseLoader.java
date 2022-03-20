package vn.aptech.doccure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... strings) throws Exception {
        try {
            Iterable<Role> roleList = roleService.findAll();
            if (!roleList.iterator().hasNext()) {
                roleService.save(new Role(Constants.Roles.ROLE_ADMIN));
                roleService.save(new Role(Constants.Roles.ROLE_USER));
            }
            if (!userService.findByEmail("admin@gmail.com").isPresent()) {
                User admin = new User();
                Set<Role> roles = new HashSet<>();
                Role roleAdmin = new Role();
                roleAdmin.setId(1L);
                roleAdmin.setName(Constants.Roles.ROLE_ADMIN);
                roles.add(roleAdmin);
                admin.setFirstName("Nguyen Ba Tuan");
                admin.setLastName("Anh");
                admin.setEmail("admin@gmail.com");
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setRoles(roles);
                admin.setEnabled(1);
                userService.save(admin);
                logger.info("Inserting user record for " + admin.getEmail());
            }
            logger.info("-------------------------------");
            if (!userService.findByEmail("user@gmail.com").isPresent()) {
                User user = new User();
                Set<Role> roles = new HashSet<>();
                Role roleAdmin = new Role();
                roleAdmin.setId(2L);
                roleAdmin.setName(Constants.Roles.ROLE_USER);
                roles.add(roleAdmin);
                user.setFirstName("Nguyen Ba Tuan");
                user.setLastName("Anh");
                user.setEmail("user@gmail.com");
                user.setUsername("anhnbt");
                user.setPassword("123456");
                user.setRoles(roles);
                user.setEnabled(1);
                userService.save(user);
                logger.info("Inserting user record for " + user.getEmail());
            }
            logger.info("-------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}