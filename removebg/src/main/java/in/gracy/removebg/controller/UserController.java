package in.gracy.removebg.controller;

import in.gracy.removebg.dto.UserDTO;
import in.gracy.removebg.response.RemoveBgResponse;
import in.gracy.removebg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create or update user
    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(
            @RequestBody UserDTO userDTO,
            Authentication authentication
    ) {

        RemoveBgResponse response;

        try {

            if (authentication == null || authentication.getName() == null) {

                response = RemoveBgResponse.builder()
                        .success(false)
                        .data("User does not have permission to access the resource")
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build();

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(response);
            }

            UserDTO user = userService.saveUser(userDTO);

            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(user)
                    .statusCode(HttpStatus.OK)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception exception) {

            exception.printStackTrace();

            response = RemoveBgResponse.builder()
                    .success(false)
                    .data(exception.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    // Get user credits
    @GetMapping("/credits")
    public ResponseEntity<?> getUserCredits(Authentication authentication) {

        RemoveBgResponse bgResponse;

        try {

            if (authentication == null || authentication.getName() == null ||
                    authentication.getName().isEmpty()) {

                bgResponse = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission/access to this resource")
                        .success(false)
                        .build();

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(bgResponse);
            }

            String clerkId = authentication.getName();

            UserDTO existingUser = userService.getUserByClerkId(clerkId);

            if (existingUser == null) {

                bgResponse = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND)
                        .data("User not found")
                        .success(false)
                        .build();

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(bgResponse);
            }

            Map<String, Integer> map = new HashMap<>();
            map.put("credits", existingUser.getCredits());

            bgResponse = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .data(map)
                    .success(true)
                    .build();

            return ResponseEntity.ok(bgResponse);

        } catch (Exception e) {

            e.printStackTrace();

            bgResponse = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Something went wrong.")
                    .success(false)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(bgResponse);
        }
    }
}