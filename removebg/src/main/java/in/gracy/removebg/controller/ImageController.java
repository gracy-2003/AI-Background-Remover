package in.gracy.removebg.controller;

import in.gracy.removebg.dto.UserDTO;
import in.gracy.removebg.response.RemoveBgResponse;
import in.gracy.removebg.service.RemoveBackgroundService;
import in.gracy.removebg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final RemoveBackgroundService removeBackgroundService;
    private final UserService userService;

    @PostMapping("/remove-background")
    public ResponseEntity<?> removeBackground(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {

        RemoveBgResponse response;
        Map<String, Object> responseMap = new HashMap<>();

        try {

            if (principal == null) {

                response = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.FORBIDDEN)
                        .success(false)
                        .data("User does not have permission/access to this resource")
                        .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            UserDTO userDTO = userService.getUserByClerkId(principal.getName());

            if (userDTO.getCredits() <= 0) {

                responseMap.put("message", "No credit balance");
                responseMap.put("creditBalance", userDTO.getCredits());

                response = RemoveBgResponse.builder()
                        .statusCode(HttpStatus.OK)
                        .success(false)
                        .data(responseMap)
                        .build();

                return ResponseEntity.ok(response);
            }

            byte[] imageBytes = removeBackgroundService.removeBackground(file);

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            userDTO.setCredits(userDTO.getCredits() - 1);

            userService.saveUser(userDTO);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(base64Image);

        } catch (Exception e) {

            e.printStackTrace();

            response = RemoveBgResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .data("Something went wrong.")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}