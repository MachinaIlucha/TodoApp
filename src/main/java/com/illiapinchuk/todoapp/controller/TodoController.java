package com.illiapinchuk.todoapp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import com.illiapinchuk.todoapp.model.Todo;
import com.illiapinchuk.todoapp.security.GetPrincipal;
import com.illiapinchuk.todoapp.service.ITodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {

    private ITodoService todoService;

    @Autowired
    public TodoController(ITodoService todoService) {
        this.todoService = todoService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping(value = "/list-todos")
    public String showTodos(ModelMap model) {
        String name = GetPrincipal.getLoggedUserName();
        model.put("todos", todoService.getTodosByUser(name));
        return "ListTodoPage";
    }

    @GetMapping(value = "/add-todo")
    public String showAddTodoPage(ModelMap model) {
        model.addAttribute("todo", new Todo());
        return "TodoPage";
    }

    @GetMapping(value = "/delete-todo")
    public String deleteTodo(@RequestParam long id) {
        todoService.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @GetMapping(value = "/update-todo")
    public String showUpdateTodoPage(@RequestParam long id, ModelMap model) {
        Todo todo = todoService.getTodoById(id).get();
        model.put("todo", todo);
        return "TodoPage";
    }

    @PostMapping(value = "/update-todo")
    public String updateTodo(@Valid Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "TodoPage";
        }

        todo.setUserName(GetPrincipal.getLoggedUserName());
        todoService.updateTodo(todo);
        return "redirect:/list-todos";
    }

    @PostMapping(value = "/add-todo")
    public String addTodo(@Valid Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "TodoPage";
        }

        todo.setUserName(GetPrincipal.getLoggedUserName());
        todoService.saveTodo(todo);
        return "redirect:/list-todos";
    }
}
