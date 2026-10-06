package in.gracy.removebg.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class RemoveBgResponse {

    private boolean success;
    private Object data;
    private HttpStatus statusCode;
    private String message;

}