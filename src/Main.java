import dao.model.User;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.registerUser(new User("Name1", "Surname1", 20, "Azerbaijan", "Baku", "user_name1", "name1.surname1@gmail.com", "password1", "05500000"));
        userService.registerUser(new User("Name2", "Surname2", 20, "Azerbaijan", "Baku", "user_name2", "name2.surname2@gmail.com", "password2", "05500000"));
        userService.registerUser(new User("Name3", "Surname3", 20, "Azerbaijan", "Baku", "user_name3", "name3.surname3@gmail.com", "password3", "05500000"));
        userService.registerUser(new User("Name4", "Surname4", 20, "Azerbaijan", "Baku", "user_name4", "name4.surname4gmail.com", "password4", "05500000"));
        userService.registerUser(new User("Name5", "Surname5", 20, "Azerbaijan", "Baku", "user_name5", "name5.surname5@gmail.com", "password5", "05500000"));

        System.out.println(userService.loginUser("name3.surname3@gmail.com", "password3"));
        System.out.println(userService.loginUser("name5.surname5@gmail.com", "password10"));
        System.out.println(userService.loginUser("name6.surname6@gmail.com", "password10"));

        User user=userService.getUserDetails("name3.surname3@gmail.com");
        System.out.println(user);
    }
}
