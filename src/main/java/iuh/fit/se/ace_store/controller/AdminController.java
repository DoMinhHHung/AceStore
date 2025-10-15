package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ace/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController  {
	private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Danh sách user", userRepository.findAll()));
    }

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<ApiResponse> disableUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(false);
                    userRepository.save(user);
                    return ResponseEntity.ok(ApiResponse.success("Đã vô hiệu hóa user."));
                })
                .orElse(ResponseEntity.ok(ApiResponse.error("Không tìm thấy user!")));
    }

	@PutMapping("/role")
	public ResponseEntity<ApiResponse> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
		ApiResponse response = userService.updateUserRole(userId, roleName);
		return ResponseEntity.ok(response);
	}
}
