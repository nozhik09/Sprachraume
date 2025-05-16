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

    @Operation(summary = "Новая категория ", description = "Добавить новую категорию")
    @PostMapping("/add")
    public Category addCategory(@RequestParam Long userId, @RequestParam String name){
        return categoryService.addCategory(userId, name);
    }

    @Operation(summary = "Все существующие категории  ", description = "Получить список всех категорий")
    @GetMapping("/allCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }


}
