package vn.aptech.doccure;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
public class DatabaseLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private ServiceService serviceService;

    @PersistenceContext
    EntityManager entityManager;

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Override
    public void run(String... strings) throws Exception {
        try {

            /**
             * Create `persistent_logins` table
             */
            Session session = entityManager.unwrap(org.hibernate.Session.class);
            session.createNativeQuery(
                    "create table if not exists persistent_logins\n" +
                            "(\n" +
                            "    username  varchar(64) not null,\n" +
                            "    series    varchar(64) not null\n" +
                            "        primary key,\n" +
                            "    token     varchar(64) not null,\n" +
                            "    last_used timestamp   not null\n" +
                            ");\n"
            ).executeUpdate();

            Iterable<Role> roleList = roleService.findAll();
            if (!roleList.iterator().hasNext()) {
                roleService.save(new Role(Constants.Roles.ROLE_ADMIN));
                roleService.save(new Role(Constants.Roles.ROLE_DOCTOR));
                roleService.save(new Role(Constants.Roles.ROLE_PATIENT));
            }

            if (roleService.findByName(Constants.Roles.ROLE_PATIENT) == null) {
                roleService.save(new Role(Constants.Roles.ROLE_PATIENT));
                logger.info("INSERT ROLE PATIENT");
            }
            if (roleService.findByName(Constants.Roles.ROLE_DOCTOR) == null) {
                roleService.save(new Role(Constants.Roles.ROLE_DOCTOR));
                logger.info("INSERT ROLE DOCTOR");
            }

            try {
                userService.findByUsername("admin");
            } catch (UsernameNotFoundException e) {
                User admin = new User();
                Set<Role> roles = new HashSet<>();
                Role roleAdmin = new Role();
                roleAdmin.setId(1L);
                roleAdmin.setName(Constants.Roles.ROLE_ADMIN);
                roles.add(roleAdmin);
                admin.setFirstName("Admin");
                admin.setLastName("Super");
                admin.setEmail("admin@gmail.com");
                admin.setUsername("administrator");
                admin.setPassword(PASSWORD_ENCODER.encode("admin"));
                admin.setRoles(roles);
                admin.setStatus(1);
                userService.save(admin);
                logger.info("Inserting user record for " + admin.getEmail());
            }
            logger.info("-------------------------------");

            try {
                userService.findByUsername("sample_patient");
            } catch (UsernameNotFoundException e) {
                User user = new User();
                Set<Role> roles = new HashSet<>();
                roles.add(roleService.findByName(Constants.Roles.ROLE_PATIENT));
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setEmail("sample_patient@gmail.com");
                user.setUsername("sample_patient");
                user.setPassword(PASSWORD_ENCODER.encode("123456"));
                user.setRoles(roles);
                user.setStatus(1);
                userService.save(user);
                logger.info("Inserting user record for " + user.getEmail());
            }
            logger.info("-------------------------------");

            try {
                userService.findByUsername("sample_doctor");
            } catch (UsernameNotFoundException e) {
                User user = new User();
                Set<Role> roles = new HashSet<>();
                roles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
                user.setFirstName("Jane");
                user.setLastName("Doe");
                user.setEmail("sample_doctor@gmail.com");
                user.setUsername("sample_doctor");
                user.setPassword(PASSWORD_ENCODER.encode("123456"));
                user.setRoles(roles);
                user.setStatus(1);
                userService.save(user);
                logger.info("Inserting user record for " + user.getEmail());
            }
//            logger.info("-------------------------------");
//            logger.info("Services");
//            Service service = serviceService.save(new Service("Tooth cleaning"));
//            logger.info("Inserting service record for " + service.getName());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}