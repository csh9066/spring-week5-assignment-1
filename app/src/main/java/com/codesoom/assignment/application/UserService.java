package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserSignupData;
import com.codesoom.assignment.dto.UserUpdateInfoData;
import com.codesoom.assignment.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User signUp(UserSignupData userSignupData) {
        User user = User.builder()
                .email(userSignupData.getEmail())
                .name(userSignupData.getName())
                .password(userSignupData.getPassword())
                .build();

        return userRepository.save(user);
    }

    /**
     * @throws UserNotFoundException 주어진 id로 User를 찾지 못했을 때 던지는 예외
     */
    public User updateInfo(Long id, UserUpdateInfoData userUpdateInfoData) {
        User user = findUser(id);

        user.updateInfo(
                userUpdateInfoData.getName(),
                userUpdateInfoData.getPassword()
        );

        return user;
    }


    /**
     * @throws UserNotFoundException 주어진 id로 User를 찾지 못했을 때 던지는 예외
     */
    public void deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
