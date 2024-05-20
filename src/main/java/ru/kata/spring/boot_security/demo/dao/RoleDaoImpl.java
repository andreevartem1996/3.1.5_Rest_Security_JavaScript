package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        return entityManager.createQuery("from Role").getResultList();
    }

    @Override
    public List<Role> getSetOfRoles(List<Role> roles) {
        List<Role> rolesFromDatabase = getRoles();

        List<String> rolesNameFromWeb = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        List<Role> rolesForUser = new ArrayList<>();

        if (rolesNameFromWeb.contains("ROLE_ADMIN")) {
            rolesFromDatabase.stream()
                    .filter(role -> role.getName().equals("ROLE_ADMIN"))
                    .forEach(rolesForUser::add);
        }

        if (rolesNameFromWeb.contains("ROLE_USER")) {
            rolesFromDatabase.stream()
                    .filter(role -> role.getName().equals("ROLE_USER"))
                    .forEach(rolesForUser::add);
        }

        return rolesForUser;
    }

}
