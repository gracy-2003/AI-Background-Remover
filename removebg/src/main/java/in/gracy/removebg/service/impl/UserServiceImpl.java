package in.gracy.removebg.service.impl;

import in.gracy.removebg.dto.UserDTO;
import in.gracy.removebg.entity.UserEntity;
import in.gracy.removebg.repository.UserRepository;
import in.gracy.removebg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {

        // Check if user exists by clerkId
        Optional<UserEntity> optionalUser =
                userRepository.findByClerkId(userDTO.getClerkId());

        UserEntity userEntity;

        if (optionalUser.isPresent()) {

            // UPDATE existing user
            userEntity = optionalUser.get();

            userEntity.setEmail(userDTO.getEmail());
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setPhotoUrl(userDTO.getPhotoUrl());

            if (userDTO.getCredits() != null) {
                userEntity.setCredits(userDTO.getCredits());
            }

        } else {

            // Check if email already exists
            Optional<UserEntity> emailUser =
                    userRepository.findByEmail(userDTO.getEmail());

            if (emailUser.isPresent()) {
                return mapToDTO(emailUser.get());
            }

            // CREATE new user
            userEntity = mapToEntity(userDTO);
        }

        UserEntity savedUser = userRepository.save(userEntity);

        return mapToDTO(savedUser);
    }

    @Override
    public void deleteUserByClerkId(String clerkId) {

        Optional<UserEntity> optionalUser =
                userRepository.findByClerkId(clerkId);

        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        }
    }

    @Override
    public UserDTO getUserByClerkId(String clerkId) {

        Optional<UserEntity> optionalUser =
                userRepository.findByClerkId(clerkId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        return mapToDTO(optionalUser.get());
    }

    private UserDTO mapToDTO(UserEntity user) {

        return UserDTO.builder()
                .clerkId(user.getClerkId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .photoUrl(user.getPhotoUrl())
                .credits(user.getCredits())
                .build();
    }

    private UserEntity mapToEntity(UserDTO userDTO) {

        return UserEntity.builder()
                .clerkId(userDTO.getClerkId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .photoUrl(userDTO.getPhotoUrl())
                .credits(userDTO.getCredits() != null ? userDTO.getCredits() : 5)
                .build();
    }
}