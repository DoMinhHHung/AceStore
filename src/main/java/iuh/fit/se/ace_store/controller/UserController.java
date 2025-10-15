package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.ChangePasswordDTO;
import iuh.fit.se.ace_store.dto.UserProfileDTO;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping("/profile")
	public ResponseEntity<ApiResponse> updateUserProfile(@RequestBody UserProfileDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		ApiResponse response = userService.updateUserProfile(email, dto);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/change-password")
	public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		ApiResponse response = userService.changePassword(email, dto);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/role")
	public ResponseEntity<ApiResponse> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
		ApiResponse response = userService.updateUserRole(userId, roleName);
		return ResponseEntity.ok(response);
	}
}
