package example.qlks.service.interfac;

import example.qlks.dto.LoginRequest;
import example.qlks.dto.Response;
import example.qlks.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}
