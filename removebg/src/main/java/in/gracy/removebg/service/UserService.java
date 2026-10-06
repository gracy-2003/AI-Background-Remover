package in.gracy.removebg.service;

import in.gracy.removebg.dto.UserDTO;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    void deleteUserByClerkId(String clerkId);

    UserDTO getUserByClerkId(String clerkId);
}
