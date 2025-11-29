// org.example.chatgateway.controller.ContactController

package org.example.chatgateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatgateway.client.AuthRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final AuthRestClient authRestClient;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("q") String query
    ) {
        List<Map<String, Object>> users = authRestClient.searchUsers(query, authHeader);
        return ResponseEntity.ok(users);
    }
}
