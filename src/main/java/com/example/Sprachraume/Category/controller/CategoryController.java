package com.example.Sprachraume.Category.controller;


import com.example.Sprachraume.Category.entity.Category;
import com.example.Sprachraume.Category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "New category", description = "Add a new category")
    @PostMapping("/add")
    public Category addCategory(@RequestParam Long userId, @RequestParam String name){
        return categoryService.addCategory(userId, name);
    }

    @Operation(summary = "All existing categories", description = "Get a list of all categories")
    @GetMapping("/allCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }


}
