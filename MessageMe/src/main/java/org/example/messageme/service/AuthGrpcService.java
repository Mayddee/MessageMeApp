package org.example.messageme.service;


import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.messageme.entity.User;
import org.example.messageme.grpc.auth.*;
import org.example.messageme.security.JwtTokenProvider;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;       // твой существующий сервис
    private final UserService userService;       // твой существующий сервис
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            // Используем твой AuthService.login(JwtRequest)
            org.example.messageme.dto.JwtRequest jwtReq =
                    new org.example.messageme.dto.JwtRequest();
            jwtReq.setUsername(request.getUsername());
            jwtReq.setPassword(request.getPassword());

            var jwtResponse = authService.login(jwtReq);

            LoginResponse response = LoginResponse.newBuilder()
                    .setId(jwtResponse.getId())
                    .setName(jwtResponse.getName())
                    .setUsername(jwtResponse.getUsername())
                    .setAccessToken(jwtResponse.getAccessToken())
                    .setRefreshToken(jwtResponse.getRefreshToken())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request,
                             StreamObserver<LoginResponse> responseObserver) {
        try {
            var jwtResponse = authService.refreshToken(request.getRefreshToken());

            LoginResponse response = LoginResponse.newBuilder()
                    .setId(jwtResponse.getId())
                    .setName(jwtResponse.getName())
                    .setUsername(jwtResponse.getUsername())
                    .setAccessToken(jwtResponse.getAccessToken())
                    .setRefreshToken(jwtResponse.getRefreshToken())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void validateToken(ValidateTokenRequest request,
                              StreamObserver<ValidateTokenResponse> responseObserver) {
        String token = request.getToken();
        try {
            boolean valid = jwtTokenProvider.validateToken(token);
            if (!valid) {
                ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                        .setValid(false)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            Long userId = Long.valueOf(jwtTokenProvider.getId(token));  // сделаем getId публичным
            User user = userService.getById(userId);

            ValidateTokenResponse.Builder builder = ValidateTokenResponse.newBuilder()
                    .setValid(true)
                    .setUserId(user.getId())
                    .setUsername(user.getUsername());

            user.getRoles().forEach(role -> builder.addRoles(role.name()));

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void registerUser(RegisterUserRequest request,
                             StreamObserver<RegisterUserResponse> responseObserver) {
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPhone(request.getPhone());
            user.setPassword(request.getPassword());

            User saved = userService.create(user);

            RegisterUserResponse response = RegisterUserResponse.newBuilder()
                    .setId(saved.getId())
                    .setName(saved.getName())
                    .setEmail(saved.getEmail())
                    .setUsername(saved.getUsername())
                    .setPhone(saved.getPhone())
                    .setEmailVerified(saved.isEmailVerified())
                    .setPhoneVerified(saved.isPhoneVerified())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}

