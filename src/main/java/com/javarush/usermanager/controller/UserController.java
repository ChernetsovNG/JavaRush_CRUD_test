package com.javarush.usermanager.controller;

import com.javarush.usermanager.model.User;
import com.javarush.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private static final int MAX_ROWS_PER_PAGE = 5;  //макс. количество записей на странице
    private static List<User> usersFindByName = new ArrayList<>();  //Список пользователей, найденных по имени
    private static boolean itWasLastFindByName = false;  //был ли последний поиск по имени пользователя?

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //Метод, разбивающий весь список на страницы
    private void pagination(Model model, @RequestParam(required = false) Integer page, PagedListHolder<User> pagedListHolder) {
        pagedListHolder.setPageSize(MAX_ROWS_PER_PAGE);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());

        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            page=1;
        }
        model.addAttribute("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            model.addAttribute("listUsers", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            model.addAttribute("listUsers", pagedListHolder.getPageList());
        }
    }

    //Метод, возвращающий страницу со всеми User'ами
    private String returnAllUsers(Model model, @RequestParam(required = false) Integer page) {
        model.addAttribute("user", new User());
        List<User> users = this.userService.listUsers();
        PagedListHolder<User> pagedListHolder = new PagedListHolder<>(users);
        pagination(model, page, pagedListHolder);
        return "users";
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String listUsers(Model model, @RequestParam(required = false) Integer page) {
        if (!itWasLastFindByName) {
            return returnAllUsers(model, page);
        }
        else {
            model.addAttribute("user", new User());
            PagedListHolder<User> pagedListHolder = new PagedListHolder<>(usersFindByName);
            pagination(model, page, pagedListHolder);
            return "users";
        }
    }

    @RequestMapping(value = "returnToAllUsers", method = RequestMethod.POST)
    public String showAllUsers(Model model, @RequestParam(required = false) Integer page) {
        itWasLastFindByName = false;
        return returnAllUsers(model, page);
    }

    //Метод, возвращающий страницу, на которой только User'ы, найденные по имени
    @RequestMapping(value = "/users/findByName", method = RequestMethod.POST)
    public String listUsersFindByName(Model model, @RequestParam(required = false) Integer page,
                                      @ModelAttribute("searchName") String name) {
        model.addAttribute("user", new User());
        List<User> allUsers = this.userService.listUsers();
        usersFindByName.clear();
        if (name.equals("") || name.equals("*")) {
            usersFindByName.addAll(allUsers);
        }
        else {
            for (User user : allUsers) {
                if (user.getName().equalsIgnoreCase(name))
                    usersFindByName.add(user);
            }
        }
        PagedListHolder<User> pagedListHolder = new PagedListHolder<>(usersFindByName);
        pagination(model, page, pagedListHolder);
        itWasLastFindByName = true;
        return "users";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user) {
        if ((user.getName().length() == 0) || (user.getAge() < 0)) {
            //do nothing...
        }
        else {
            if (user.getId() == 0) {
                this.userService.addUser(user);
            }
            else {
                this.userService.updateUser(user);
            }
        }
        itWasLastFindByName=false;
        return "redirect:/users";
    }

    @RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") int id) {
        this.userService.removeUser(id);
        itWasLastFindByName=false;
        return "redirect:/users";
    }

    @RequestMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model, @RequestParam(required = false) Integer page) {
        model.addAttribute("user", this.userService.getUserById(id));

        List<User> users = this.userService.listUsers();
        PagedListHolder<User> pagedListHolder = new PagedListHolder<>(users);

        pagination(model, page, pagedListHolder);

        return "users";
    }

    @RequestMapping("userdata/{id}")
    public String userData(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "userdata";
    }

}
