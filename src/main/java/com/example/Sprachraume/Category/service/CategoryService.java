package com.example.Sprachraume.Category.service;


import com.example.Sprachraume.Category.entity.Category;
import com.example.Sprachraume.Category.CategoryRepository;
import com.example.Sprachraume.Exceptions.Exception.AlreadyUsedException;
import com.example.Sprachraume.Exceptions.Exception.InvalidRoleException;
import com.example.Sprachraume.Exceptions.Exception.UserNotFoundException;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public Category addCategory(Long userId, String name) {

        UserData userData = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not Found"));
        if (userIsAdmin(userData)) {
            throw new InvalidRoleException("Only admin can add new Category");
        }
        if (categoryRepository.findByName(name).isPresent()) {
            throw new AlreadyUsedException("Category with name '" + name + "' already exists");
        }


        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }


    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    private boolean userIsAdmin(UserData user) {
        for (Role role : user.getRoles()) {
            if ("ADMIN".equals(role.getTitle())) {
                return true;
            }
        }
        return false;
    }


}
